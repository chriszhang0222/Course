package com.chris.service;

import com.chris.domain.*;
import com.chris.dto.*;
import com.chris.dto.subpagedto.CoursePageDto;
import com.chris.enums.CourseStatus;
import com.chris.mapper.CourseContentMapper;
import com.chris.mapper.CourseMapper;
import com.chris.mapper.my.MyCourseMapper;
import com.chris.util.CopyUtil;
import com.chris.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CourseService {

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private MyCourseMapper myCourseMapper;

    @Resource
    private CourseCategoryService courseCategoryService;

    @Resource
    private CourseContentMapper courseContentMapper;

    @Resource
    private TeacherService teacherService;

    @Resource
    private ChapterService chapterService;

    @Resource
    private SectionService sectionService;

    public void list(CoursePageDto coursePageDto){
        PageHelper.startPage(coursePageDto.getPage(), coursePageDto.getSize());
        CourseExample courseExample = new CourseExample();
        List<Course> courses = courseMapper.selectByExample(courseExample);
        PageInfo<Course> pageInfo = new PageInfo<>(courses);
        coursePageDto.setTotal(pageInfo.getTotal());
        List<CourseDto> dtos = courses.stream().map(c -> {
            CourseDto dto = new CourseDto();
            BeanUtils.copyProperties(c, dto);
            return dto;
        }).collect(Collectors.toList());
        coursePageDto.setList(dtos);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(CourseDto courseDto){
        Course course = new Course();
        BeanUtils.copyProperties(courseDto, course);
        if(StringUtils.isEmpty(courseDto.getId())){
            insert(course);
        }else{
            update(course);
        }
        courseCategoryService.saveBatch(course.getId(), courseDto.getCategories());
    }

    public void insert(Course course){
        course.setCreateAt(new Date());
        course.setUpdateAt(new Date());
        course.setId(UuidUtil.getShortUuid());
        courseMapper.insert(course);
    }

    public void update(Course course){
        course.setUpdateAt(new Date());
        courseMapper.updateByPrimaryKey(course);
    }

    public void delete(String id){
        CourseCategoryExample example = new CourseCategoryExample();
        courseMapper.deleteByPrimaryKey(id);
        courseCategoryService.deleteByCourseId(id);
    }

    public void updateTime(String courseId){
        log.info("Update course:{} time", courseId);
        myCourseMapper.updateTime(courseId);
    }

    public CourseContentDto findContent(String id){
        CourseContent courseContent = courseContentMapper.selectByPrimaryKey(id);
        if(courseContent == null)
            return null;
        return CopyUtil.copy(courseContent, CourseContentDto.class);
    }

    public int saveContent(CourseContentDto courseContentDto){
        CourseContent content = CopyUtil.copy(courseContentDto, CourseContent.class);
        int i = courseContentMapper.updateByPrimaryKeyWithBLOBs(content);
        if(i == 0){
            i = courseContentMapper.insert(content);
        }
        return i;
    }

    public CourseDto findCourse(String id){
        Course course = courseMapper.selectByPrimaryKey(id);
        if(course == null || !CourseStatus.PUBLISH.getCode().equals(course.getStatus())){
            return null;
        }
        CourseDto courseDto = CopyUtil.copy(course, CourseDto.class);
        CourseContent content = courseContentMapper.selectByPrimaryKey(id);
        if(content != null){
            courseDto.setContent(content.getContent());
        }
        TeacherDto teacherDto = teacherService.findById(courseDto.getTeacherId());
        courseDto.setTeacher(teacherDto);

        List<ChapterDto> chapterDtos = chapterService.listByCourse(id);
        courseDto.setChapters(chapterDtos);

        List<SectionDto> sectionDtos = sectionService.listByCourse(id);
        courseDto.setSections(sectionDtos);
        return courseDto;
    }

    @Transactional
    public void sort(SortDto sortDto){
        myCourseMapper.updateSort(sortDto);
        if(sortDto.getNewSort() > sortDto.getOldSort()){
            myCourseMapper.moveSortsForward(sortDto);
        }
        else if(sortDto.getNewSort() < sortDto.getOldSort()){
            myCourseMapper.moveSortsBackward(sortDto);
        }
    }
}
