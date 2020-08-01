package com.chris.controller.admin;

import com.chris.dto.FileDto;
import com.chris.dto.PageDto;
import com.chris.enums.FileUse;
import com.chris.service.FileService;
import com.chris.util.Base64ToMultipartFile;
import com.chris.util.CommonResponse;
import com.chris.util.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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

    @RequestMapping("/upload1")
    public FileDto upload1(@RequestParam MultipartFile file, String use) throws Exception{
        log.info("Start uploading file:{}", file);
        log.info(String.valueOf(file.getSize()));

        FileUse fileUse = FileUse.getByCode(use);

        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
        String key = UuidUtil.getShortUuid();
        String newFilePath = fileUse.getDesc().toLowerCase() + "/" + key + "." + suffix;
        String fullpath = filePath + newFilePath;

        createDirIfNotExist(fileUse.getDesc().toLowerCase());

        File dest = new File(fullpath);
        file.transferTo(dest);
        log.info(dest.getAbsolutePath());
        FileDto fileDto = saveFile(fileName, newFilePath, file, suffix);
        fileDto.setPath(fileDomain + newFilePath);
        return fileDto;
    }

    @RequestMapping("/upload")
    public FileDto upload(@RequestBody FileDto fileDto) throws Exception{
        String use = fileDto.getUse();
        String key = fileDto.getKey();
        String suffix = fileDto.getSuffix();
        String shardBase64 = fileDto.getShard();
        MultipartFile shard = Base64ToMultipartFile.base64ToMultipart(shardBase64);

        FileUse fileUseenum = FileUse.getByCode(use);
        String dir = fileUseenum.getDesc().toLowerCase();
        createDirIfNotExist(dir);
        String path = new StringBuilder(dir)
                .append(File.separator)
                .append(key)
                .append(".")
                .append(suffix)
                .toString();
        String localPath = new StringBuilder(path)
                .append(".")
                .append(fileDto.getShardIndex())
                .toString();
        String fullPath = filePath + localPath;
        File dest = new File(fullPath);
        shard.transferTo(dest);
        log.info(dest.getAbsolutePath());
        log.info("Save File Start");

        fileDto.setPath(fileDomain + path);
        fileService.save(fileDto);
        if(fileDto.getShardIndex().equals(fileDto.getShardTotal())){
            this.merge(fileDto);
        }
        return fileDto;
    }

    public void merge(FileDto fileDto) throws Exception{
        log.info("Merge Slice Start!!");
        String path = fileDto.getPath();
        path = path.replace(fileDomain, "");
        Integer shardTotal = fileDto.getShardTotal();
        File newFile = new File(filePath + path);
        FileOutputStream outputStream = new FileOutputStream(newFile, true);
        FileInputStream fileInputStream = null;
        byte[] byt = new byte[10 * 1024 * 1024];
        int len;
        try{
            for(int i=0; i<shardTotal; i++){
                fileInputStream = new FileInputStream(new File(filePath + path + "." + (i+1)));
                while((len = fileInputStream.read(byt)) != -1){
                    outputStream.write(byt, 0, len);
                }
            }
        }catch (IOException e){
            log.error(e.getMessage());
        }finally {
            try{
                if(fileInputStream != null){
                    fileInputStream.close();
                }
                outputStream.close();
                log.info("IO Stream Close!");
            }catch (Exception e){
                log.error("IO stream Close", e);
            }
        }
        log.info("Merge Slice completed!");
        System.gc();
        Thread.sleep(100);

        for(int i=0;i<shardTotal;i++){
            String filepath = filePath + path + "." + (i+1);
            File file = new File(filepath);
            boolean res = file.delete();
            log.info("Delete{}, {}", filepath, res);
        }
        log.info("Delete slice completed!");
    }

    @GetMapping("/check/{key}")
    public CommonResponse check(@PathVariable String key){
        CommonResponse response = new CommonResponse();
        FileDto fileDto = fileService.findByKey(key);
        response.setContent(fileDto);
        return response;
    }


    public void createDirIfNotExist(String dir){
        File fullDir = new File(filePath + dir);
        if(!fullDir.exists()){
            fullDir.mkdir();
        }
    }

    public FileDto saveFile(String fileName, String filePath, MultipartFile file, String suffix){
        FileDto fileDto = new FileDto();
        fileDto.setName(fileName);
        fileDto.setPath(filePath);
        fileDto.setSize(Math.toIntExact(file.getSize()));
        fileDto.setSuffix(suffix);
        fileDto.setUse("");
        fileService.save(fileDto);
        return fileDto;
    }

    @PostMapping("/list")
    public PageDto list(@RequestBody PageDto pageDto){
        fileService.list(pageDto);
        return pageDto;
    }
}
