package com.chris.controller.admin;

import com.chris.dto.FileDto;
import com.chris.dto.PageDto;
import com.chris.enums.FileUse;
import com.chris.service.FileService;
import com.chris.util.CommonResponse;
import com.chris.util.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

@RestController
@RequestMapping("/admin")
@Slf4j
public class FileController {

    @Value("${file.path}")
    private String filePath;

    @Value("${file.domain}")
    private String fileDomain;

    @Resource
    private FileService fileService;

    @RequestMapping("/upload")
    public CommonResponse upload(@RequestParam MultipartFile file, String use) throws Exception{
        log.info("Start uploading file:{}", file);
        log.info(String.valueOf(file.getSize()));

        FileUse fileUse = FileUse.getByCode(use);

        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
        String key = UuidUtil.getShortUuid();
        String newFilePath = "teacher/" + key + "." + suffix;
        String fullpath = filePath + newFilePath;

        createDirIfNotExist(fileUse.getDesc().toLowerCase());

        File dest = new File(fullpath);
        file.transferTo(dest);
        log.info(dest.getAbsolutePath());
        saveFile(fileName, newFilePath, file, suffix);

        return new CommonResponse("200", "OK", fileDomain + newFilePath);
    }

    public void createDirIfNotExist(String dir){
        File fullDir = new File(filePath + dir);
        if(!fullDir.exists()){
            fullDir.mkdir();
        }
    }

    public void saveFile(String fileName, String filePath, MultipartFile file, String suffix){
        FileDto fileDto = new FileDto();
        fileDto.setName(fileName);
        fileDto.setPath(filePath);
        fileDto.setSize(Math.toIntExact(file.getSize()));
        fileDto.setSuffix(suffix);
        fileDto.setUse("");
        fileService.save(fileDto);
    }

    @PostMapping("/list")
    public PageDto list(@RequestBody PageDto pageDto){
        fileService.list(pageDto);
        return pageDto;
    }
}
