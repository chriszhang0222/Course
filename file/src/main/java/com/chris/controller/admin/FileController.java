package com.chris.controller.admin;

import com.chris.util.CommonResponse;
import com.chris.util.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/admin")
@Slf4j
public class FileController {
    private String file_url = "http://127.0.0.1:9003/file/f/teacher/";

    @Value("${file.path}")
    private String filePath;

    @Value("${file.domain}")
    private String fileDomain;

    @RequestMapping("/upload")
    public CommonResponse upload(@RequestParam MultipartFile file) throws Exception{
        log.info("Start uploading file:{}", file);
        log.info(String.valueOf(file.getSize()));

        String fileName = file.getOriginalFilename();
        String key = UuidUtil.getShortUuid();
        String fullpath = filePath + key + "-" + fileName;
        File dest = new File(fullpath);
        file.transferTo(dest);
        log.info(dest.getAbsolutePath());
        return new CommonResponse("200", "OK", fileDomain + "teacher/" + key + "-" + fileName);
    }
}
