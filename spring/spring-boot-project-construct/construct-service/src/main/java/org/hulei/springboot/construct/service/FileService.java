package org.hulei.springboot.construct.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;

/**
 * @author hulei
 * @since 2024/11/15 17:00
 */

@Slf4j
@Component
public class FileService {

    @SneakyThrows
    public void getFile(){
        InputStream bootstrapStream = FileService.class.getClassLoader().getResourceAsStream("templates/application.yml");
        File bootstrapFile = new File("bootstrap.yml");
        FileUtils.copyInputStreamToFile(bootstrapStream, bootstrapFile);
        log.info(bootstrapFile.getAbsolutePath());
    }

    @SneakyThrows
    public static void main(String[] args) {
        FileService fileService = new FileService();
        fileService.getFile();
    }
}
