package com.hundsun.demo.springboot.utils;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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


    private static final String UPLOAD_DIR = "uploads";

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {

        // 文件上传可能超过大小. 配置这两个参数
        // spring.servlet.multipart.max-file-size=10MB 指定了单个文件上传的最大大小限制
        // spring.servlet.multipart.max-request-size=10MB 指定了整个 multipart 请求的最大大小限制

        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload", HttpStatus.BAD_REQUEST);
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path uploadPath = Paths.get(UPLOAD_DIR);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            return new ResponseEntity<>("File uploaded successfully: " + fileName, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload file: " + fileName, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public String index() {
        return "Welcome to file upload example. Use /upload to upload a file.";
    }
}

