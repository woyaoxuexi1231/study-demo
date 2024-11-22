package org.hulei.springboot.utils;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author hulei
 * @since 2024/11/21 14:53
 */

@RequestMapping("/itext-pdf")
@RestController
public class ItextPdfController {

    @GetMapping("/export-pdf")
    public void exportPdf(HttpServletRequest request, HttpServletResponse response) {
        try {

            // Step 1: 创建文档对象
            Document document = new Document();
            // Step 2: 创建PDF写入器
            PdfWriter.getInstance(document, response.getOutputStream());

            // Step 3: 打开文档
            document.open();

            // Step 4: 设置字体支持中文
            BaseFont baseFont = BaseFont.createFont("Noto Sans SC", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font chineseFont = new Font(baseFont, 12, Font.NORMAL);

            // 添加标题
            document.add(new Paragraph("生成PDF范例", chineseFont));
            document.add(new Paragraph("使用iText支持中文的PDF生成。", chineseFont));

            // 添加表格
            PdfPTable table = new PdfPTable(3); // 3列表格
            table.addCell(new PdfPCell(new Phrase("列1", chineseFont)));
            table.addCell(new PdfPCell(new Phrase("列2", chineseFont)));
            table.addCell(new PdfPCell(new Phrase("列3", chineseFont)));
            for (int i = 1; i <= 9; i++) {
                table.addCell(new PdfPCell(new Phrase("单元格 " + i, chineseFont)));
            }
            document.add(table);

            // 添加图片（可选）
            // Image image = Image.getInstance("path_to_image.jpg"); // 替换为你的图片路径
            // image.scaleToFit(200, 200); // 设置图片大小
            // document.add(image);

            // 添加超链接
            Anchor link = new Anchor("点击打开百度", chineseFont);
            link.setReference("https://www.baidu.com");
            document.add(link);

            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("test.pdf", StandardCharsets.UTF_8));


            // Step 5: 关闭文档
            document.close();


            System.out.println("PDF生成成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
