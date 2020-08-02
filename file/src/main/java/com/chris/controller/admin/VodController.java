package com.chris.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetMezzanineInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.chris.dto.FileDto;
import com.chris.enums.FileUse;
import com.chris.service.FileService;
import com.chris.util.Base64ToMultipartFile;
import com.chris.util.CommonResponse;
import com.chris.util.VodUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.codec.binary.Base64;
import javax.annotation.Resource;


@RestController
@RequestMapping("/admin")
@Slf4j
public class VodController {

    @Value("${vod.accessKeyId}")
    private String accessKeyId;

    @Value("${vod.accessKeySecret}")
    private String accessKeySecret;

    @Resource
    private FileService fileService;

    @PostMapping("/vod")
    public FileDto fileUpload(@RequestBody FileDto fileDto){
        String use = fileDto.getUse();
        String key = fileDto.getKey();
        String suffix = fileDto.getSuffix();
        String shardBase64 = fileDto.getShard();

        MultipartFile shard = Base64ToMultipartFile.base64ToMultipart(shardBase64);

        FileUse useEnum = FileUse.getByCode(use);
        String dir = useEnum.getDesc().toLowerCase();

        String path = new StringBuffer(dir)
                .append("/")
                .append(key)
                .append(".")
                .append(suffix)
                .toString();
        String vod = "";
        String fileUrl = "";
        try{
            DefaultAcsClient vodClient = VodUtil.initVodClient(accessKeyId, accessKeySecret);
            CreateUploadVideoResponse createUploadVideoResponse = VodUtil.createUploadVideo(vodClient, path);
            vod = createUploadVideoResponse.getVideoId();
            JSONObject uplpadAuth = JSONObject.parseObject(
                    Base64.decodeBase64(createUploadVideoResponse.getUploadAuth()),
                    JSONObject.class
            );
            JSONObject uploadAddress = JSONObject.parseObject(
                    Base64.decodeBase64(createUploadVideoResponse.getUploadAddress()),
                    JSONObject.class
            );
            OSSClient ossClient = VodUtil.initOssClient(uplpadAuth, uploadAddress);
            VodUtil.uploadLocalFile(ossClient, uploadAddress, shard.getInputStream());
            log.info("File upload to oss successfully!");
            GetMezzanineInfoResponse response = VodUtil.getMezzanineInfo(vodClient, vod);
            System.out.println("获取视频信息, response : " + JSON.toJSONString(response));
            fileUrl = response.getMezzanine().getFileURL();
            ossClient.shutdown();

        }catch (Exception e){
            log.error("Upload to Ali Oss failed: ", e.getMessage());
        }

        fileDto.setPath(fileUrl);
        fileDto.setVod(vod);
        fileService.save(fileDto);
        return fileDto;
    }

    @GetMapping("/get-auth/{vod}")
    public CommonResponse getAuth(@PathVariable String vod) throws ClientException, com.aliyuncs.exceptions.ClientException {
        CommonResponse commonResponse = new CommonResponse();
        DefaultAcsClient client = VodUtil.initVodClient(accessKeyId, accessKeySecret);
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        try{
            response = VodUtil.getVideoPlayAuth(client, vod);
            log.info("auth code = {}", response.getPlayAuth());
            commonResponse.setContent(response.getPlayAuth());
            log.info("VideoMeta = {}", JSON.toJSONString(response.getVideoMeta()));
        }catch (Exception e){
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        return commonResponse;
    }

}
