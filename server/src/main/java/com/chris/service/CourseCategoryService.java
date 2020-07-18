package com.chris.service;

import com.chris.domain.Course;
import com.chris.domain.CourseCategory;
import com.chris.domain.CourseCategoryExample;
import com.chris.dto.CategoryDto;
import com.chris.dto.CourseCategoryDto;
import com.chris.dto.PageDto;
import com.chris.mapper.CourseCategoryMapper;
import com.chris.util.CopyUtil;
import com.chris.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class CourseCategoryService {
    @Resource
    private CourseCategoryMapper courseCategoryMapper;

    public void list(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        CourseCategoryExample courseCategoryExample = new CourseCategoryExample();
        List<CourseCategory> courseCategoryList = courseCategoryMapper.selectByExample(courseCategoryExample);
        PageInfo<CourseCategory> pageInfo = new PageInfo<>(courseCategoryList);
        pageDto.setTotal(pageInfo.getTotal());
        List<CourseCategoryDto> courseCategoryDtoList = CopyUtil.copyList(courseCategoryList, CourseCategoryDto.class);
        pageDto.setList(courseCategoryDtoList);
    }

    public void save(CourseCategoryDto courseCategoryDto){
        CourseCategory courseCategory = CopyUtil.copy(courseCategoryDto, CourseCategory.class);
        if(StringUtils.isEmpty(courseCategory.getId())){
            courseCategory.setId(UuidUtil.getShortUuid());
            courseCategoryMapper.insert(courseCategory);
        }else{
            courseCategoryMapper.updateByPrimaryKey(courseCategory);
        }
    }

    public void delete(String id){
        courseCategoryMapper.deleteByPrimaryKey(id);
    }

    private void insert(CourseCategory courseCategory){
        courseCategory.setId(UuidUtil.getShortUuid());
        courseCategoryMapper.insert(courseCategory);
    }

    public void saveBatch(String courseId, List<CategoryDto> categoryDtos){
        CourseCategoryExample example = new CourseCategoryExample();
        example.createCriteria().andCourseIdEqualTo(courseId);
        courseCategoryMapper.deleteByExample(example);
        for(CategoryDto categoryDto: categoryDtos){
            CourseCategory courseCategory = new CourseCategory();
            courseCategory.setCategoryId(categoryDto.getId());
            courseCategory.setCourseId(courseId);
            insert(courseCategory);
        }
    }

    public void deleteByCourseId(String courseId){
        CourseCategoryExample example = new CourseCategoryExample();
        example.createCriteria().andCourseIdEqualTo(courseId);
        courseCategoryMapper.deleteByExample(example);
    }

    public List<CourseCategoryDto> listByCourse(String courseId){
        CourseCategoryExample example = new CourseCategoryExample();
        example.createCriteria().andCourseIdEqualTo(courseId);
        List<CourseCategory> courseCategoryList = courseCategoryMapper.selectByExample(example);
        List<CourseCategoryDto> courseCategoryDtos = CopyUtil.copyList(courseCategoryList, CourseCategoryDto.class);
        return courseCategoryDtos;
    }
}
