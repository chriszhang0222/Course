package com.chris.controller.admin;

import com.alibaba.fastjson.JSON;
import com.chris.domain.Chapter;
import com.chris.dto.ChapterDto;
import com.chris.dto.PageDto;
import com.chris.service.ChapterService;
import com.chris.util.CommonResponse;
import com.chris.util.ValidatorUtil;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: Chris Zhang
 * @Date: 5/3/20 10:34
 */
@RestController
@RequestMapping("/admin/chapter")
@Slf4j
public class ChapterController {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Car{
        String brand;
        String name;
        String price;

    }

    @Autowired
    private ChapterService chapterService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public PageDto getChapterList(@RequestBody PageDto pageDto){
        chapterService.list(pageDto);
        return pageDto;
    }

    @GetMapping(value="/test")
    public CommonResponse<String> getMapping(){
        return new CommonResponse<String>("200", "OK", "Hello World");
    }

    @GetMapping("/hello")
    public List<String> getList(){
        List<String> lst = new ArrayList<>(Arrays.asList(new String[]{"Ok", "Fine"}));
        return lst;
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ChapterDto save(@RequestBody ChapterDto chapterDto){
        ValidatorUtil.require(chapterDto.getName(), "Name");
        ValidatorUtil.require(chapterDto.getCourseId(), "CourseID");
        ValidatorUtil.length(chapterDto.getCourseId(), "CourseID", 1, 8);
        log.info("chapterDto:{}", JSON.toJSONString(chapterDto));
        chapterService.insert(chapterDto);
        return chapterDto;
    }

    @RequestMapping("/update")
    public ChapterDto update(@RequestBody ChapterDto chapterDto){
        log.info("chapterDto:{}", JSON.toJSONString(chapterDto));
        chapterService.insert(chapterDto);
        return chapterDto;
    }

    @RequestMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable String id){
        log.info("delete:{}", id);
        chapterService.delete(id);
        return new CommonResponse("200","OK");
    }
}
