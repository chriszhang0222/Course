package com.chris.service;

import com.chris.domain.Chapter;
import com.chris.domain.ChapterExample;
import com.chris.dto.ChapterDto;
import com.chris.dto.PageDto;
import com.chris.mapper.ChapterMapper;
import com.chris.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    public void list(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        ChapterExample example = new ChapterExample();
        List<Chapter> chapters = chapterMapper.selectByExample(example);
        PageInfo<Chapter> pageInfo= new PageInfo<>(chapters);
        pageDto.setTotal(pageInfo.getTotal());
        List<ChapterDto> dtoList = chapters.stream().map(c -> {
            ChapterDto dto = new ChapterDto();
            BeanUtils.copyProperties(c,dto);
            return dto;
        }).collect(Collectors.toList());
        pageDto.setList(dtoList);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void insert(ChapterDto chapterDto){
        Chapter chapter = new Chapter();
        BeanUtils.copyProperties(chapterDto, chapter);
        if(StringUtils.isBlank(chapter.getId())){
            chapter.setId(UuidUtil.getShortUuid());
            chapterMapper.insert(chapter);
        }else{
            chapterMapper.updateByPrimaryKey(chapter);
        }

    }

    public void delete(String id){
        chapterMapper.deleteByPrimaryKey(id);
    }




}
