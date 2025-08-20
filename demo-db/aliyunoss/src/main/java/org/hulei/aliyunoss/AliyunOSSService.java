package org.hulei.aliyunoss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hulei.aliyunoss.config.AliyunOSSConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliyunOSSService {
    private final OSS ossClient;
    private final AliyunOSSConfig ossConfig;

    /**
     * 上传文件
     * @param file 文件对象
     * @param folder 存储文件夹
     * @return 文件访问URL
     */
    public String uploadFile(MultipartFile file, String folder) {
        try {
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = folder + "/" + UUID.randomUUID() + fileExtension;
            
            // 上传文件到OSS
            ossClient.putObject(ossConfig.getBucketName(), fileName, new ByteArrayInputStream(file.getBytes()));
            
            // 生成访问URL（可设置过期时间）
            Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000); // 1小时后过期
            URL url = ossClient.generatePresignedUrl(ossConfig.getBucketName(), fileName, expiration);
            
            return url.toString();
        } catch (IOException e) {
            log.error("文件上传失败: {}", e.getMessage());
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 下载文件
     * @param fileName 文件名
     * @return 文件流
     */
    public InputStream downloadFile(String fileName) {
        OSSObject ossObject = ossClient.getObject(ossConfig.getBucketName(), fileName);
        return ossObject.getObjectContent();
    }

    /**
     * 删除文件
     * @param fileName 文件名
     */
    public void deleteFile(String fileName) {
        ossClient.deleteObject(ossConfig.getBucketName(), fileName);
        log.info("文件删除成功: {}", fileName);
    }
}