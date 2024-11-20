package org.hulei.springboot.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * @author hulei
 * @since 2024/11/20 11:53
 */

@Slf4j
@RequestMapping("/shell-executor")
@RestController
public class ShellExecutorController {

    @GetMapping("/runtime-process")
    public void runtime() {
        try {
            // 指定脚本路径
            Process process = Runtime.getRuntime().exec("sh ./script.sh");

            // 获取脚本的输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }

            // 等待脚本执行完成
            int exitCode = process.waitFor();
            log.info("Exit Code: {}", exitCode);
        } catch (Exception e) {
            log.error("{}", e.getMessage(), e);
        }
    }

    @GetMapping("/processBuilder")
    public void processBuilder(){
        try {
            // 使用 ProcessBuilder 调用脚本
            ProcessBuilder pb = new ProcessBuilder(Arrays.asList("sh", "./script.sh"));
            pb.redirectErrorStream(true); // 合并标准错误流和标准输出流

            Process process = pb.start();

            // 读取脚本的输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }

            // 等待脚本执行完成
            int exitCode = process.waitFor();
            log.info("Exit Code: {}", exitCode);
        } catch (Exception e) {
            log.error("{}", e.getMessage(), e);
        }
    }


    @GetMapping("/commons-exec")
    public void commonsExec(){
        try {
            // 创建命令行对象
            CommandLine cmd = new CommandLine("sh");
            cmd.addArgument("./script.sh");

            // 执行脚本
            DefaultExecutor executor = DefaultExecutor.builder().get();
            int exitCode = executor.execute(cmd);
            log.info("Exit Code: {}", exitCode);
        } catch (Exception e) {
            log.error("{}", e.getMessage(), e);
        }
    }
}
