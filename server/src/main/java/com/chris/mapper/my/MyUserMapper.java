package com.chris.mapper.my;

import com.chris.dto.ResourceDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyUserMapper {

    List<ResourceDto> findResources(@Param("userId") String userId);
}
