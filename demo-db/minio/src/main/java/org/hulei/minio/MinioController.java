package org.hulei.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/minio")
public class MinioController {

    private final MinioService minioService;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public MinioController(MinioService minioService) {
        this.minioService = minioService;
    }

    // 文件上传接口
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> result = new HashMap<>();
        try {
            String objectName = minioService.uploadFile(file);
            // 这里返回的是存储在 MinIO 中的对象名称
            // 如果你配置了外网可访问的域名，可以拼接成完整 URL
            result.put("message", "文件上传成功");
            result.put("objectName", objectName);
            // 如果你想返回可下载链接，可以调用 getFileUrl(objectName, 3600)
            // result.put("downloadUrl", minioService.getFileUrl(objectName, 3600));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("error", "文件上传失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }

    // 获取文件下载链接（示例）
    @GetMapping("/url/{objectName}")
    public ResponseEntity<Map<String, String>> getFileUrl(@PathVariable String objectName) {
        Map<String, String> result = new HashMap<>();
        try {
            String url = minioService.getFileUrl(objectName, 3600); // 1小时有效
            result.put("downloadUrl", url);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("error", "获取下载链接失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }

    /**
     * 下载 MinIO 中的文件，并保存到本地服务器
     *
     * @param objectName MinIO 中的文件 Key，比如 "images/avatar.jpg"
     * @return 返回保存到本地的文件路径
     */
    @GetMapping("/download-to-local")
    public ResponseEntity<String> downloadFileToLocal(@RequestParam String objectName) {
        try {
            String localPath = minioService.downloadFileToLocal(objectName);
            return ResponseEntity.ok("文件已成功下载到本地，路径：" + localPath);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("下载失败: " + e.getMessage());
        }
    }

    /**
     * 直接从 MinIO 下载文件并返回给客户端（浏览器下载）
     *
     * @param objectName MinIO 中的文件 Key，如 "images/avatar.jpg" 或 "docs/report.pdf"
     * @return 文件流响应，浏览器会触发下载
     */
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFileFromMinio(
            @RequestParam String objectName) {
        try {
            // 1. 从 MinIO 获取文件输入流
            InputStream inputStream = minioService.getFileStream(objectName);

            // 2. 设置文件名（用于浏览器下载时显示的文件名）
            // 注意：如果 objectName 包含路径比如 "docs/report.pdf"，你可以提取 "report.pdf" 作为下载文件名
            String downloadFileName = objectName.substring(objectName.lastIndexOf('/') + 1);

            // 3. 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" +
                    URLEncoder.encode(downloadFileName, StandardCharsets.UTF_8));

            // 你可以根据实际文件类型设置 Content-Type，或者使用通用的 application/octet-stream
            // 如果知道具体类型，比如 PDF 是 application/pdf，图片是 image/jpeg 等，可以动态设置
            MediaType contentType = MediaType.APPLICATION_OCTET_STREAM; // 默认二进制流
            // 如果是 PDF，可以这样：MediaType.parseMediaType("application/pdf")

            // 4. 构造响应体
            InputStreamResource resource = new InputStreamResource(inputStream);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(contentType)
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @Resource
    private MinioClient minioClient;

    @GetMapping("/file-exist")
    public void fileExist(@RequestParam(value = "filePath") String filePath) {
        boolean exists = minioService.doesObjectExist(filePath);
        if (exists) {
            System.out.println("文件存在！");
        } else {
            System.out.println("文件不存在！");
        }
    }
}