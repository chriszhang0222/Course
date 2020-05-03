package com.chris.service;

import com.chris.domain.Chapter;
import com.chris.domain.ChapterExample;
import com.chris.dto.ChapterDto;
import com.chris.mapper.ChapterMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Chris Zhang
 * @Date: 5/3/20 10:32
 */
@Service
public class ChapterService {

    @Resource
    private ChapterMapper chapterMapper;

    public List<ChapterDto> list(){
        ChapterExample example = new ChapterExample();
        List<Chapter> chapters = chapterMapper.selectByExample(example);
        List<ChapterDto> dtoList = chapters.stream().map(c -> {
            ChapterDto dto = new ChapterDto();
            BeanUtils.copyProperties(c,dto);
            return dto;
        }).collect(Collectors.toList());
        return dtoList;
    }


}
