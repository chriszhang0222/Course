package com.chris.controller.admin;

import com.chris.domain.Chapter;
import com.chris.dto.ChapterDto;
import com.chris.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Chris Zhang
 * @Date: 5/3/20 10:34
 */
@RestController
@RequestMapping("/admin")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;


    @RequestMapping("/chapter")
    public List<ChapterDto> getChapterList(){
        return chapterService.list();
    }
}
