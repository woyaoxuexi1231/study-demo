package org.hulei.minio;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class MinioService {

    @Resource
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    // 可配置：下载的文件保存到哪个本地目录
    @Value("${minio.download-dir:/tmp/minio-downloads}") // 默认保存到 /tmp/minio-downloads
    private String downloadDir;

    // 检查存储桶是否存在，不存在则创建
    public void ensureBucketExists() throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            System.out.println("Bucket '" + bucketName + "' created.");
        } else {
            System.out.println("Bucket '" + bucketName + "' already exists.");
        }
    }

    // 上传文件
    public String uploadFile(MultipartFile file) throws Exception {
        ensureBucketExists();

        String originalFilename = file.getOriginalFilename();
        String objectName = UUID.randomUUID().toString() + "-" + originalFilename; // 避免文件名冲突


        InputStream io = file.getInputStream();
        minioClient.putObject(
                PutObjectArgs.builder()
                        /*
                        Bucket（桶）是 MinIO 中用于存储对象（文件）的顶级容器，你可以把它理解为“文件夹”或“存储空间”的顶层目录，但它实际上是一个逻辑隔离的存储单元。
                        Bucket（桶） 一个仓库 / 一个硬盘分区 / 一个顶级文件夹  用于存放一堆文件（对象），是对象的“大容器”
                         */
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(io, file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        io.close();
        // 返回访问 URL（如果你配置了 MinIO 的外网访问和域名，这里可以返回完整可访问链接）
        return objectName;
    }

    // 获取文件下载链接（临时，有有效期）
    public String getFileUrl(String objectName, int expiresInSeconds) throws Exception {
        ensureBucketExists();
        String url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(expiresInSeconds)
                        .build()
        );
        return url.toString();
    }

    // 删除文件
    public void deleteFile(String objectName) throws Exception {
        ensureBucketExists();
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

    // 列出所有 Bucket（可选）
    public List<Bucket> listBuckets() throws Exception {
        return minioClient.listBuckets();
    }

    /**
     * 从 MinIO 下载文件，并保存到本地磁盘
     *
     * @param objectName MinIO 中的文件 Key（比如 "images/photo.jpg"）
     * @return 下载后保存的本地文件路径
     */
    public String downloadFileToLocal(String objectName) throws Exception {
        // 确保下载目录存在
        Path downloadPath = Paths.get(downloadDir);
        if (!Files.exists(downloadPath)) {
            Files.createDirectories(downloadPath);
        }

        // 定义本地保存的文件路径
        String localFilePath = downloadDir + File.separator + objectName.replace("/", "_"); // 避免路径问题
        File localFile = new File(localFilePath);

        // 从 MinIO 下载文件流
        try (InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build())) {

            // 将输入流保存成本地文件
            Files.copy(inputStream, localFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        System.out.println("文件已下载到本地: " + localFilePath);
        return localFilePath;
    }

    /**
     * 从 MinIO 获取文件输入流
     * @param objectName MinIO 中的文件 Key，如 "docs/report.pdf"
     * @return 文件的输入流
     */
    public InputStream getFileStream(String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }
}