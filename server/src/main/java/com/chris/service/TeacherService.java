package com.chris.service;

import com.chris.domain.Teacher;
import com.chris.domain.TeacherExample;
import com.chris.dto.PageDto;
import com.chris.dto.TeacherDto;
import com.chris.mapper.TeacherMapper;
import com.chris.util.CopyUtil;
import com.chris.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TeacherService {

    @Resource
    private TeacherMapper teacherMapper;

    public List<TeacherDto> all(){
        TeacherExample example = new TeacherExample();
        List<Teacher> teacherList = teacherMapper.selectByExample(example);
        return CopyUtil.copyList(teacherList, TeacherDto.class);
    }

    public void list(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        TeacherExample example = new TeacherExample();
        List<Teacher> teacherList = teacherMapper.selectByExample(example);
        PageInfo<Teacher> pageInfo = new PageInfo<>(teacherList);
        pageDto.setTotal(pageInfo.getTotal());
        List<TeacherDto> dtoList = CopyUtil.copyList(teacherList, TeacherDto.class);
        pageDto.setList(dtoList);
    }

    public void save(TeacherDto dto){
        Teacher teacher = CopyUtil.copy(dto, Teacher.class);
        if(StringUtils.isEmpty(teacher.getId())){
            insert(teacher);
        }else{
            update(teacher);
        }
    }

    private void insert(Teacher teacher){
        teacher.setId(UuidUtil.getShortUuid());
        teacherMapper.insert(teacher);
    }

    private void update(Teacher teacher){
        teacherMapper.updateByPrimaryKey(teacher);
    }

    public void delete(String id){
        teacherMapper.deleteByPrimaryKey(id);
    }

    public TeacherDto findById(String id){
        Teacher teacher = teacherMapper.selectByPrimaryKey(id);
        return CopyUtil.copy(teacher, TeacherDto.class);
    }

}
