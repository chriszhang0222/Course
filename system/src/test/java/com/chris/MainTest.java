package com.chris;

import com.chris.domain.Chapter;
import com.chris.mapper.ChapterMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: Chris Zhang
 * @Date: 5/3/20 10:02
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class MainTest {

    @Autowired
    private ChapterMapper chapterMapper;

    @Test
    public void test(){

        Chapter c = chapterMapper.selectByPrimaryKey("1");
        log.info("{}", c.getName());

    }
}
