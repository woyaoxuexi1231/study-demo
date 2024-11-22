package org.hulei.springboot.utils;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.net.URL;
import java.util.Objects;

public class ITextExample {
    public static void main(String[] args) {
        String outputPath = "example.pdf";

        try {
            // Step 1: 创建文档对象
            Document document = new Document();
            // Step 2: 创建PDF写入器
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));

            // Step 3: 打开文档
            document.open();

            // Step 4: 设置字体支持中文
            BaseFont baseFont = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            BaseFont msYaHeiBaseFont = BaseFont.createFont("经典宋体简.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont harmonyOSBaseFont = BaseFont.createFont("HarmonyOS_Sans_Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font chineseFont = new Font(baseFont, 12, Font.NORMAL);

            // // 添加标题
            // document.add(new Paragraph("生成PDF范例", chineseFont));
            // document.add(new Paragraph("使用iText支持中文的PDF生成。", chineseFont));
            //
            // // 添加表格
            // PdfPTable table = new PdfPTable(3); // 3列表格
            // table.addCell(new PdfPCell(new Phrase("列1", chineseFont)));
            // table.addCell(new PdfPCell(new Phrase("列2", chineseFont)));
            // table.addCell(new PdfPCell(new Phrase("列3", chineseFont)));
            // for (int i = 1; i <= 9; i++) {
            //     table.addCell(new PdfPCell(new Phrase("单元格 " + i, chineseFont)));
            // }
            // document.add(table);

            // document.newPage();
            // 添加超链接
            Anchor link = new Anchor("点击打开百度", chineseFont);
            link.setReference("https://www.baidu.com");
            document.add(link);


            // 添加图片（可选）
            // ClassLoader classLoader = ITextExample.class.getClassLoader();
            // URL logoUrl = classLoader.getResource("OIP.jpg");
            //
            // Image image = Image.getInstance(Objects.requireNonNull(logoUrl)); // 替换为你的图片路径
            // image.scaleToFit(200, 200); // 设置图片大小
            // document.add(image);

            document.newPage();

            // Step 5: 关闭文档
            document.close();

            System.out.println("PDF生成成功: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
