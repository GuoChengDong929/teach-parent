package com.access.business.academic.curriculum.controller;

import com.access.business.academic.curriculum.service.ChapterService;
import com.teach.aop.Log;
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

    @Log("章节列表")
    @RequiresPermissions("api-chapter-select")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public Result list(@RequestBody Map<String,Object> map){
        return chapterService.list(map);
    }

    @Log("查询全部章节")
    @RequestMapping(value = "/findChapters",method = RequestMethod.GET)
    public Result findChapters(){
        return  chapterService.findChapters();
    }

    @Log("保存章节")
    @RequiresPermissions("api-chapter-add")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result save(@RequestBody Chapter chapter){
        return chapterService.save(chapter);
    }

    @Log("修改章节")
    @RequiresPermissions("api-chapter-update")
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public Result update(@RequestBody Chapter chapter){
        return chapterService.update(chapter);
    }


    //通过阶段查询当前阶段的所有章节及章节下的所有试题数据集合
    @Log("通过阶段ID查询当前阶段的所有章节")
    @RequestMapping(value = "/getChapterList/{curriculumId}",method = RequestMethod.POST)
    public Result getChapterList(@PathVariable("curriculumId")String curriculumId) throws CommonException {
        return chapterService.getChapterList(curriculumId);
    }
}
