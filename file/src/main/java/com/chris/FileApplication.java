package com.chris;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
@MapperScan("com.chris.mapper")
@Slf4j
public class FileApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FileApplication.class);
        Environment env = app.run(args).getEnvironment();
        log.info("Init File！！");
        log.info("File Address: \thttp://127.0.0.1:{}", env.getProperty("server.port"));
    }
}
