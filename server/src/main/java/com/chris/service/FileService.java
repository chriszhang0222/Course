package com.chris.service;

import com.chris.domain.File;
import com.chris.domain.FileExample;
import com.chris.dto.FileDto;
import com.chris.dto.PageDto;
import com.chris.mapper.FileMapper;
import com.chris.util.CopyUtil;
import com.chris.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class FileService {

    @Resource
    private FileMapper fileMapper;

    public void list(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        FileExample example = new FileExample();
        List<File> fileList = fileMapper.selectByExample(example);
        PageInfo<File> pageInfo = new PageInfo<>(fileList);
        pageDto.setTotal(pageInfo.getTotal());
        List<FileDto> fileDtos = CopyUtil.copyList(fileList, FileDto.class);
        pageDto.setList(fileDtos);
    }

    public void save(FileDto fileDto){
        File file = CopyUtil.copy(fileDto, File.class);
        File fileDb = selectByKey(fileDto.getKey());
        if(fileDb == null){
            insert(file);
        }else{
            fileDb.setShardIndex(fileDto.getShardIndex());
            update(fileDb);
        }
    }

    private void insert(File file){
        Date date = new Date();
        file.setCreateAt(date);
        file.setUpdateAt(date);
        file.setId(UuidUtil.getShortUuid());
        fileMapper.insert(file);
    }

    private void update(File file){
        Date date = new Date();
        file.setUpdateAt(date);
        fileMapper.updateByPrimaryKey(file);
    }

    public void delete(String id){
        fileMapper.deleteByPrimaryKey(id);
    }

    public File selectByKey(String key){
        FileExample example = new FileExample();
        example.createCriteria().andKeyEqualTo(key);
        List<File> files = fileMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(files))
            return null;
        return files.get(0);
    }

    public FileDto findByKey(String key){
        return CopyUtil.copy(selectByKey(key), FileDto.class);
    }
}
