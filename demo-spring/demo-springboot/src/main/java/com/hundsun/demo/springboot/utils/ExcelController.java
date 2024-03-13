package com.hundsun.demo.springboot.utils;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.controller
 * @Description:
 * @Author: hulei42031
 * @Date: 2024/1/16 15:50
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@RestController
@RequestMapping("/api")
public class ExcelController {

    @GetMapping("/export-excel")
    public ResponseEntity<byte[]> exportExcel() {
        Workbook workbook = new XSSFWorkbook();

        // 在这里添加代码生成 Excel 文件，例如：
        // Sheet sheet = workbook.createSheet("Sheet1");
        // ... (添加数据到表格中)

        try (FileOutputStream fileOut = new FileOutputStream("workbook.xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        HttpHeaders headers = new HttpHeaders();
        /*
        Content-Disposition 头部来指示客户端将响应视为文件的附件，并欲以什么名称来保存。
        attachment 意味着要求浏览器以附件方式下载响应
        filename=workbook.xlsx 则指定了默认的下载文件名
         */
        headers.add("Content-Disposition", "attachment;filename=workbook.xlsx");

        return new ResponseEntity<>(getFileBytes("workbook.xlsx"), headers, HttpStatus.OK);
    }

    private byte[] getFileBytes(String filename) {
        // 实现根据文件名获取文件字节数组的逻辑
        // 在这里添加代码将文件转换为字节数组
        // 返回文件字节数组
        return null;
    }
}

