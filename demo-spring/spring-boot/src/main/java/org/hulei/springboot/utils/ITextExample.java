package org.hulei.springboot.utils;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ITextExample {

    public static void main(String[] args) {

        String outputPath = "example.pdf";

        try {
            // Step 1: 创建文档对象
            Document document = new Document();
            // Step 2: 创建PDF写入器
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            writer
                    .setPageEvent(new CustomHeaderEvent());


            // Step 3: 打开文档
            document.open();

            // Step 4: 设置字体支持中文
            BaseFont baseFont = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            BaseFont msYaHeiBaseFont = BaseFont.createFont("/itext-pdf/经典宋体简.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont harmonyOSBaseFont = BaseFont.createFont("/itext-pdf/HarmonyOS_Sans_Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font chineseFont = new Font(harmonyOSBaseFont, 12, Font.NORMAL);

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

            // 获取当前时间
            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            Font footerFont = new Font(baseFont, 8, Font.NORMAL, BaseColor.BLACK);

            // 页脚内容
            String footerText = "姓名: " + "张三" + "    时间: " + currentDate;

            // 使用 PdfContentByte 添加页脚内容
            PdfContentByte canvas = writer.getDirectContent();
            ColumnText.showTextAligned(
                    canvas,
                    Element.ALIGN_RIGHT, // 右对齐
                    new Phrase(footerText, footerFont),
                    document.right(), // 右边距
                    document.bottom(), // 下边距
                    0 // 无旋转
            );

            // Step 5: 关闭文档
            document.close();

            System.out.println("PDF生成成功: " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 自定义页眉事件
    private static class CustomHeaderEvent extends PdfPageEventHelper {
        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                // 从 resources 文件夹加载图片
                ClassLoader classLoader = getClass().getClassLoader();
                String logoPath = Objects.requireNonNull(classLoader.getResource("itext-pdf/itextPdf.jpg")).getPath(); // 图片文件名为 logo.png
                Image logo = Image.getInstance(logoPath);
                logo.scaleToFit(50, 50); // 设置图片大小
                logo.setAbsolutePosition(20, document.top() + 10); // 位置

                // 获取直接内容
                PdfContentByte cb = writer.getDirectContent();

                // 添加图片到页眉
                cb.addImage(logo);

                // 获取直接内容
                Phrase headerText = new Phrase("This is the header text");

                // 添加图片到页眉
                cb.addImage(logo);

                ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, headerText,
                        (document.right() - document.left()+72) / 2,
                        document.top() + 10, 0);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
