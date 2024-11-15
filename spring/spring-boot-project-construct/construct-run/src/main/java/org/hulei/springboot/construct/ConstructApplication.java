package org.hulei.springboot.construct;

import lombok.RequiredArgsConstructor;
import org.hulei.springboot.construct.service.FileService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2024/11/15 16:41
 */

@RequiredArgsConstructor
@RestController
@SpringBootApplication
public class ConstructApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConstructApplication.class, args);
    }

    private final FileService fileService;

    @GetMapping("/getFile")
    public void getFile() {
        fileService.getFile();
    }

}
