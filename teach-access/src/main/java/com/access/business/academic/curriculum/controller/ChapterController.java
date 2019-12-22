package com.access.business.academic.curriculum.controller;

import com.access.business.academic.curriculum.service.ChapterService;
import com.teach.entity.academic.curriculum.Chapter;
import com.teach.error.CommonException;
import com.teach.response.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
//
@RequestMapping("/chapter")
@RestController
public class ChapterController {


    @Autowired
    private ChapterService chapterService;

    @RequiresPermissions("api-section-select")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public Result list(@RequestBody Map<String,Object> map){
        return chapterService.list(map);
    }


    @RequestMapping(value = "/findChapters",method = RequestMethod.GET)
    public Result findChapters(){
        return  chapterService.findChapters();
    }

    @RequiresPermissions("api-section-add")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result save(@RequestBody Chapter chapter){
        return chapterService.save(chapter);
    }

    @RequiresPermissions("api-section-update")
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public Result update(@RequestBody Chapter chapter){
        return chapterService.update(chapter);
    }


    //通过阶段查询当前阶段的所有章节及章节下的所有试题数据集合
    @RequestMapping(value = "/getChapterList/{curriculumId}",method = RequestMethod.POST)
    public Result getChapterList(@PathVariable("curriculumId")String curriculumId) throws CommonException {
        return chapterService.getChapterList(curriculumId);
    }
}
