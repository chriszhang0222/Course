package com.chris.service;

import com.chris.domain.Section;
import com.chris.domain.SectionExample;
import com.chris.dto.ChapterDto;
import com.chris.dto.PageDto;
import com.chris.dto.SectionDto;
import com.chris.dto.subpagedto.SectionPageDto;
import com.chris.mapper.SectionMapper;
import com.chris.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {

    @Resource
    private SectionMapper sectionMapper;

    @Resource
    private CourseService courseService;

    public void list(SectionPageDto pageDto){
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        SectionExample example = new SectionExample();
        SectionExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isBlank(pageDto.getChapterId())){
            criteria.andChapterIdEqualTo(pageDto.getChapterId());
        }
        example.setOrderByClause("sort asc");
        List<Section> sectionList = sectionMapper.selectByExample(example);
        PageInfo<Section> pageInfo = new PageInfo<>(sectionList);
        pageDto.setTotal(pageInfo.getTotal());
        List<SectionDto> dtoList = sectionList.stream().map(section -> {
            SectionDto sectionDto = new SectionDto();
            BeanUtils.copyProperties(section, sectionDto);
            return sectionDto;
        }).collect(Collectors.toList());
        pageDto.setList(dtoList);
    }

    @Transactional
    public void save(SectionDto sectionDto){
        Section section = new Section();
        BeanUtils.copyProperties(sectionDto, section);
        if(StringUtils.isEmpty(section.getId())){
            insert(section);
        }else{
            update(section);
        }
        if(!StringUtils.isEmpty(section.getCourseId())) {
            courseService.updateTime(section.getCourseId());
        }
    }

    private void insert(Section section){
        Date now = new Date();
        section.setCreateAt(now);
        section.setUpdateAt(now);
        section.setId(UuidUtil.getShortUuid());
        sectionMapper.insert(section);
    }

    private void update(Section section){
        section.setUpdateAt(new Date());
        sectionMapper.updateByPrimaryKey(section);
    }

    public void delete(String id){
        sectionMapper.deleteByPrimaryKey(id);
    }


}
