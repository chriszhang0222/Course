package com.chris.controller;

import com.chris.dto.PageDto;
import com.chris.dto.ResourceDto;
import com.chris.service.ResourceService;
import com.chris.util.CommonResponse;
import com.chris.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/resource")
@Slf4j
public class ResourceController {

    @Resource
    private ResourceService resourceService;

    @PostMapping("/list")
    public PageDto list(@RequestBody PageDto pageDto){
        resourceService.list(pageDto);
        return pageDto;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable String id) {
        resourceService.delete(id);
        return new CommonResponse();
    }

    @PostMapping("/save")
    public CommonResponse save(@RequestBody String jsonStr){
        ValidatorUtil.require(jsonStr, "resource");
        resourceService.saveJson(jsonStr);
        return new CommonResponse();
    }

    @GetMapping("/load-tree")
    public CommonResponse loadTree(){
        List<ResourceDto> resourceDtoList = resourceService.loadTree();
        CommonResponse commonResponse = new CommonResponse("200", "OK");
        commonResponse.setContent(resourceDtoList);
        return commonResponse;
    }


}
