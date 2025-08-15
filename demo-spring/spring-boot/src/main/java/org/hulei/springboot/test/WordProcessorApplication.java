package org.hulei.springboot.test;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

// @SpringBootApplication
@RestController
public class WordProcessorApplication {

    // 模拟大模型API调用的函数
    private String callLLMApi(String text) {
        try {
            Thread.sleep(500); // 模拟网络延迟
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // 模拟精简内容
        String simplified = text.length() > 50 ? text.substring(0, 47) + "..." : text;
        System.out.println("Original: " + text);
        System.out.println("Simplified: " + simplified);
        return simplified;
    }

    @PostMapping("/process-word")
    public void processWordDocument(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException, ExecutionException, InterruptedException {
        if (file.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "File is empty");
            return;
        }

        try (XWPFDocument document = new XWPFDocument(file.getInputStream())) {
            List<ParagraphData> paragraphsData = new ArrayList<>();

            // 第一步：读取所有段落及其格式和类型（正文/非正文）
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                // 尝试从 目录后的内容 开始精简
                ParagraphData pData = new ParagraphData();
                pData.setParagraph(paragraph);

                // --- 判断是否为正文 ---
                boolean isBodyText = isBodyTextParagraph(paragraph);
                pData.setBodyText(isBodyText);

                StringBuilder textBuilder = new StringBuilder();
                RunFormat firstRunFormat = null;
                boolean firstRun = true;
                for (XWPFRun run : paragraph.getRuns()) {
                    String runText = run.getText(0);
                    if (runText != null) {
                        textBuilder.append(runText);
                    }
                    if (firstRun && runText != null && !runText.isEmpty()) {
                        firstRunFormat = captureRunFormat(run);
                        firstRun = false;
                    }
                }
                pData.setOriginalText(textBuilder.toString());
                pData.setFirstRunFormat(firstRunFormat);
                if (!isBodyText && pData.getOriginalText().matches(".*参考文献.*")) {
                    break;
                }
                paragraphsData.add(pData);
            }

            // 第二步：并发调用大模型API处理 **正文** 内容
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (ParagraphData pData : paragraphsData) {
                // --- 仅处理正文段落 ---
                if (pData.isBodyText()) {
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        String originalText = pData.getOriginalText();
                        if (originalText != null && !originalText.trim().isEmpty()) {
                            String simplifiedText = callLLMApi(originalText);
                            pData.setSimplifiedText(simplifiedText);
                        } else {
                            pData.setSimplifiedText(originalText);
                        }
                    });
                    futures.add(future);
                }
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            // 第三步：将精简后的内容写回原段落，保留原始格式
            for (ParagraphData pData : paragraphsData) {
                XWPFParagraph paragraph = pData.getParagraph();
                // --- 根据是否为正文决定写入原始文本还是简化文本 ---
                String finalText = pData.isBodyText() ? pData.getSimplifiedText() : pData.getOriginalText();

                RunFormat formatToApply = pData.getFirstRunFormat();

                // 清除原段落的所有Run
                List<XWPFRun> runs = new ArrayList<>(paragraph.getRuns());
                for (XWPFRun run : runs) {
                    paragraph.removeRun(paragraph.getRuns().indexOf(run));
                }

                // 创建新的Run并设置文本和格式
                if (finalText != null) {
                    XWPFRun newRun = paragraph.createRun();
                    newRun.setText(finalText);

                    // 应用之前保存的格式 (仅对非空段落)
                    if (formatToApply != null && !finalText.isEmpty()) {
                        applyRunFormat(newRun, formatToApply);
                    }
                }
            }

            // 第四步：返回修改后的文档
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            response.setHeader("Content-Disposition", "attachment; filename=processed_document.docx");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.write(outputStream);
            response.getOutputStream().write(outputStream.toByteArray());
            response.getOutputStream().flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to process document: " + e.getMessage());
        }
    }

    /**
     * 判断一个段落是否为正文。
     * 通常，正文段落使用默认的 "Normal" 样式。
     *
     * @param paragraph 要判断的XWPFParagraph对象
     * @return 如果是正文则返回true，否则返回false
     */
    private boolean isBodyTextParagraph(XWPFParagraph paragraph) {
        String style = paragraph.getStyle();
        // System.out.println("Paragraph Style: '" + style + "'"); // 调试用

        // 如果样式ID为空或"Normal"（不区分大小写），则认为是正文
        // 不同语言版本的Word可能使用不同的"Normal"翻译，例如中文可能是"正文"
        // 这里提供一个基础的判断，你可能需要根据实际文档调整
        if (style == null || style.isEmpty() || "Normal".equalsIgnoreCase(style) || "正文".equals(style)) {
            return true;
        }

        return false;

        // 可以添加更多非正文样式的判断，例如标题样式
        // 使用正则表达式匹配常见的标题样式
        // if (Pattern.matches("(Heading|header|标题)\\s*\\d*", style)) {
        //     return false;
        // }

        // 如果以上条件都不满足，可以默认认为是正文，
        // 或者根据更复杂的逻辑（如段落位置、字体大小等）进一步判断
        // 这里为了简化，暂时默认返回true。
        // 在实际应用中，可能需要更细致的规则。
        // 例如，检查段落是否在特定节中，或者其格式是否与已知标题格式匹配等。
        // 作为示例，我们假设除了明确的标题外，其他都是正文。
        // 更保险的做法可能是维护一个非正文样式列表。
        // 但"Normal"是最常见的正文样式。

        // 假设除了已知的非正文样式，其他都算正文
        // 这是一个简化的处理，实际项目中可能需要更精确的判断逻辑
        // return true; // 或者根据更复杂的规则返回false
    }


    // 辅助类：用于存储Run的格式信息
    private static class RunFormat {
        private Boolean bold;
        private Boolean italic;
        private String color;
        private String fontFamily;
        private Integer fontSize;

        public Boolean getBold() {
            return bold;
        }

        public void setBold(Boolean bold) {
            this.bold = bold;
        }

        public Boolean getItalic() {
            return italic;
        }

        public void setItalic(Boolean italic) {
            this.italic = italic;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getFontFamily() {
            return fontFamily;
        }

        public void setFontFamily(String fontFamily) {
            this.fontFamily = fontFamily;
        }

        public Integer getFontSize() {
            return fontSize;
        }

        public void setFontSize(Integer fontSize) {
            this.fontSize = fontSize;
        }
    }

    // 捕获 XWPFRun 的格式信息
    private RunFormat captureRunFormat(XWPFRun run) {
        RunFormat format = new RunFormat();
        try {
            format.setBold(run.isBold());
            format.setItalic(run.isItalic());
            format.setColor(run.getColor());
            format.setFontFamily(run.getFontFamily());
            int fontSize = run.getFontSize();
            if (fontSize != -1) {
                format.setFontSize(fontSize);
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not capture all format properties for a run: " + e.getMessage());
        }
        return format;
    }

    // 将保存的格式信息应用到新的 XWPFRun
    private void applyRunFormat(XWPFRun run, RunFormat format) {
        if (format == null) return;

        if (format.getBold() != null && format.getBold()) {
            run.setBold(true);
        }
        if (format.getItalic() != null && format.getItalic()) {
            run.setItalic(true);
        }
        if (format.getColor() != null) {
            run.setColor(format.getColor());
        }
        if (format.getFontFamily() != null) {
            run.setFontFamily(format.getFontFamily());
        }
        if (format.getFontSize() != null) {
            run.setFontSize(format.getFontSize());
        }
    }

    // 存储段落数据的辅助类
    private static class ParagraphData {
        private XWPFParagraph paragraph;
        private String originalText;
        private String simplifiedText;
        private RunFormat firstRunFormat;
        private boolean isBodyText = false; // 新增属性

        public XWPFParagraph getParagraph() {
            return paragraph;
        }

        public void setParagraph(XWPFParagraph paragraph) {
            this.paragraph = paragraph;
        }

        public String getOriginalText() {
            return originalText;
        }

        public void setOriginalText(String originalText) {
            this.originalText = originalText;
        }

        public String getSimplifiedText() {
            return simplifiedText;
        }

        public void setSimplifiedText(String simplifiedText) {
            this.simplifiedText = simplifiedText;
        }

        public RunFormat getFirstRunFormat() {
            return firstRunFormat;
        }

        public void setFirstRunFormat(RunFormat firstRunFormat) {
            this.firstRunFormat = firstRunFormat;
        }

        public boolean isBodyText() {
            return isBodyText;
        } // Getter

        public void setBodyText(boolean bodyText) {
            isBodyText = bodyText;
        } // Setter
    }

    public static void main(String[] args) {
        SpringApplication.run(WordProcessorApplication.class, args);
    }
}



