package org.hulei.aliyunoss.test;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.hulei.aliyunoss.config.AliyunOSSConfig;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordProcessingService {

    private final OSS ossClient;
    private final AliyunOSSConfig ossConfig;

    /**
     * 处理 Word 文件：下载 → 处理 → 上传
     */
    public String processWordFile(String sourceFileKey) {
        try {
            // 1. 从 OSS 下载文件
            File downloadedFile = downloadFromOSS(sourceFileKey);
            
            // 2. 处理 Word 文件内容
            File processedFile = processWordContent(downloadedFile);
            
            // 3. 上传处理后的文件到 OSS
            String newFileKey = generateNewFileKey(sourceFileKey);
            uploadToOSS(processedFile, newFileKey);
            
            // 4. 清理临时文件
            cleanupTempFiles(downloadedFile, processedFile);
            
            return getFileUrl(newFileKey);
            
        } catch (Exception e) {
            log.error("Word 文件处理失败", e);
            throw new RuntimeException("文件处理失败", e);
        }
    }

    /**
     * 从 OSS 下载文件到本地临时文件
     */
    private File downloadFromOSS(String fileKey) throws IOException {
        OSSObject ossObject = ossClient.getObject(ossConfig.getBucketName(), fileKey);
        
        File tempFile = File.createTempFile("download_", ".docx");
        try (InputStream inputStream = ossObject.getObjectContent();
             FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        
        log.info("文件下载完成: {}", tempFile.getAbsolutePath());
        return tempFile;
    }

    /**
     * 处理 Word 文件内容
     */
    private File processWordContent(File inputFile) throws IOException {
        File outputFile = File.createTempFile("processed_", ".docx");
        
        try (FileInputStream fis = new FileInputStream(inputFile);
             XWPFDocument document = new XWPFDocument(fis);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            
            // 处理段落
            processParagraphs(document.getParagraphs());
            
            // 处理表格中的段落
            processTables(document.getTables());
            
            // 处理页眉
            processHeaders(document.getHeaderList());
            
            // 处理页脚
            processFooters(document.getFooterList());
            
            // 保存处理后的文档
            document.write(fos);
        }
        
        log.info("Word 文件处理完成: {}", outputFile.getAbsolutePath());
        return outputFile;
    }

    /**
     * 处理正文段落
     */
    private void processParagraphs(List<XWPFParagraph> paragraphs) {
        for (XWPFParagraph paragraph : paragraphs) {
            if (isMainContentParagraph(paragraph)) {
                processSingleParagraph(paragraph);
            }
        }
    }

    /**
     * 判断是否是正文段落（可根据需要自定义规则）
     */
    private boolean isMainContentParagraph(XWPFParagraph paragraph) {
        String text = paragraph.getText().trim();
        
        // 排除空段落
        if (text.isEmpty()) {
            return false;
        }
        
        // 排除标题（根据样式判断）
        String style = paragraph.getStyle();
        if (style != null && (style.contains("Heading") || style.contains("Title"))) {
            return false;
        }
        
        // 排除页眉页脚内容（通常包含特定文本）
        if (text.contains("第") && text.contains("页") || 
            text.contains("Page") || 
            text.contains("Copyright")) {
            return false;
        }
        
        return true;
    }

    /**
     * 处理单个段落内容
     */
    private void processSingleParagraph(XWPFParagraph paragraph) {
        String originalText = paragraph.getText();
        
        if (originalText.trim().isEmpty()) {
            return;
        }
        
        // 这里实现你的删减逻辑
        String processedText = reduceContent(originalText);
        
        // 清除原有运行（runs）
        for (int i = paragraph.getRuns().size() - 1; i >= 0; i--) {
            paragraph.removeRun(i);
        }
        
        // 添加新的运行（保持原有格式）
        XWPFRun newRun = paragraph.createRun();
        newRun.setText(processedText);
        
        // 保持原有格式（如果有的话）
        if (!paragraph.getRuns().isEmpty()) {
            XWPFRun originalRun = paragraph.getRuns().get(0);
            copyRunStyle(originalRun, newRun);
        }
    }

    /**
     * 内容删减逻辑（根据需求自定义）
     */
    private String reduceContent(String text) {
        // 示例1: 删除敏感信息
        text = removeSensitiveInfo(text);
        
        // 示例2: 截断过长的段落
        if (text.length() > 500) {
            text = text.substring(0, 500) + "...";
        }
        
        // 示例3: 移除特定关键词
        text = text.replace("机密信息", "***")
                  .replace("内部资料", "***");
        
        return text;
    }

    /**
     * 移除敏感信息（示例）
     */
    private String removeSensitiveInfo(String text) {
        // 移除手机号
        text = text.replaceAll("1[3-9]\\d{9}", "***");
        
        // 移除身份证号
        text = text.replaceAll("[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]", "***");
        
        // 移除邮箱
        text = text.replaceAll("\\b[\\w.-]+@[\\w.-]+\\.\\w{2,}\\b", "***");
        
        return text;
    }

    /**
     * 复制运行样式
     */
    private void copyRunStyle(XWPFRun source, XWPFRun target) {
        target.setBold(source.isBold());
        target.setItalic(source.isItalic());
        target.setUnderline(source.getUnderline());
        target.setColor(source.getColor());
        target.setFontFamily(source.getFontFamily());
        target.setFontSize(source.getFontSize());
    }

    /**
     * 处理表格中的段落
     */
    private void processTables(List<XWPFTable> tables) {
        for (XWPFTable table : tables) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    processParagraphs(cell.getParagraphs());
                }
            }
        }
    }

    /**
     * 处理页眉
     */
    private void processHeaders(List<XWPFHeader> headers) {
        // 页眉通常不需要处理，保持原样
        log.debug("跳过页眉处理");
    }

    /**
     * 处理页脚
     */
    private void processFooters(List<XWPFFooter> footers) {
        // 页脚通常不需要处理，保持原样
        log.debug("跳过页脚处理");
    }

    /**
     * 上传文件到 OSS
     */
    private void uploadToOSS(File file, String fileKey) throws FileNotFoundException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            ossClient.putObject(ossConfig.getBucketName(), fileKey, inputStream);
            log.info("文件上传成功: {}", fileKey);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 生成新的文件key
     */
    private String generateNewFileKey(String originalKey) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomId = UUID.randomUUID().toString().substring(0, 8);
        
        if (originalKey.contains(".")) {
            int lastDotIndex = originalKey.lastIndexOf(".");
            String name = originalKey.substring(0, lastDotIndex);
            String extension = originalKey.substring(lastDotIndex);
            return name + "_processed_" + timestamp + "_" + randomId + extension;
        } else {
            return originalKey + "_processed_" + timestamp + "_" + randomId + ".docx";
        }
    }

    /**
     * 获取文件URL
     */
    private String getFileUrl(String fileKey) {
        return "https://" + ossConfig.getBucketName() + "." + ossConfig.getEndpoint() + "/" + fileKey;
    }

    /**
     * 清理临时文件
     */
    private void cleanupTempFiles(File... files) {
        for (File file : files) {
            if (file != null && file.exists()) {
                if (file.delete()) {
                    log.debug("临时文件已删除: {}", file.getAbsolutePath());
                } else {
                    log.warn("无法删除临时文件: {}", file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * 处理上传的 Word 文件（如果是从前端上传）
     */
    public String processUploadedWordFile(MultipartFile file) {
        try {
            // 保存上传文件到临时文件
            File tempInputFile = File.createTempFile("upload_", ".docx");
            file.transferTo(tempInputFile);
            
            // 处理文件
            File processedFile = processWordContent(tempInputFile);
            
            // 上传到OSS
            String fileKey = "processed/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            uploadToOSS(processedFile, fileKey);
            
            // 清理临时文件
            cleanupTempFiles(tempInputFile, processedFile);
            
            return getFileUrl(fileKey);
            
        } catch (Exception e) {
            log.error("处理上传的Word文件失败", e);
            throw new RuntimeException("文件处理失败", e);
        }
    }
}