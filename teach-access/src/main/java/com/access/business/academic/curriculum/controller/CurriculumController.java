package com.access.business.academic.curriculum.controller;

import com.access.business.academic.curriculum.service.CurriculumService;
import com.teach.entity.academic.curriculum.Curriculum;
import com.teach.response.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/curriculum")
@RestController
public class CurriculumController {

    @Autowired
    private CurriculumService curriculumService;

    @RequiresPermissions("api-class-select")
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public Result findAll(){
        return curriculumService.findAll();
    }

    @RequiresPermissions("api-class-add")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result save(@RequestBody Curriculum curriculum){
        return curriculumService.save(curriculum);
    }

    @RequiresPermissions("api-class-update")
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public Result update(@RequestBody Curriculum curriculum){
        return curriculumService.update(curriculum);
    }

    //不给权限,学生刷题列表
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public Result list(@RequestBody Map<String,Object> map){
        return curriculumService.list(map);
    }
}

