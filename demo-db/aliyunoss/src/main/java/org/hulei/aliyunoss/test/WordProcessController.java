package org.hulei.aliyunoss.test;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/word")
@RequiredArgsConstructor
public class WordProcessController {

    private final WordProcessingService wordProcessingService;

    /**
     * 处理 OSS 上的 Word 文件
     */
    @PostMapping("/process-oss-file")
    public String processOssFile(@RequestParam String fileKey) {
        return wordProcessingService.processWordFile(fileKey);
    }

    /**
     * 处理上传的 Word 文件
     */
    @PostMapping("/process-upload-file")
    public String processUploadFile(@RequestParam("file") MultipartFile file) {
        if (!file.getOriginalFilename().toLowerCase().endsWith(".docx")) {
            throw new RuntimeException("只支持 .docx 格式的文件");
        }
        
        return wordProcessingService.processUploadedWordFile(file);
    }

    /**
     * 批量处理 OSS 文件
     */
    @PostMapping("/batch-process")
    public String batchProcess(@RequestParam String[] fileKeys) {
        StringBuilder result = new StringBuilder();
        for (String fileKey : fileKeys) {
            try {
                String processedUrl = wordProcessingService.processWordFile(fileKey);
                result.append("文件: ").append(fileKey)
                     .append(" → 处理成功: ").append(processedUrl)
                     .append("\n");
            } catch (Exception e) {
                result.append("文件: ").append(fileKey)
                     .append(" → 处理失败: ").append(e.getMessage())
                     .append("\n");
            }
        }
        return result.toString();
    }
}