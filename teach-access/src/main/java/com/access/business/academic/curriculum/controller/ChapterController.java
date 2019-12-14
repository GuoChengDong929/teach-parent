package com.access.business.academic.curriculum.controller;

import com.access.business.academic.curriculum.service.ChapterService;
import com.teach.entity.academic.curriculum.Chapter;
import com.teach.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/chapter")
@RestController
public class ChapterController {


    @Autowired
    private ChapterService chapterService;

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public Result list(@RequestBody Map<String,Object> map){
        return chapterService.list(map);
    }


    @RequestMapping(value = "/findChapters",method = RequestMethod.GET)
    public Result findChapters(){
        return  chapterService.findChapters();
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result save(@RequestBody Chapter chapter){
        return chapterService.save(chapter);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public Result update(@RequestBody Chapter chapter){
        return chapterService.update(chapter);
    }

}
