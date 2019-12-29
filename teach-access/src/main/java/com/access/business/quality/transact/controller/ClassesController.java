package com.access.business.quality.transact.controller;

import com.access.business.quality.transact.service.ClassesService;
import com.teach.aop.Log;
import com.teach.entity.quality.transact.Classes;
import com.teach.response.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/classes")
public class ClassesController {

    @Autowired
    private ClassesService classesService;

    @Log("查询班级列表")
    @RequiresPermissions("api-classes-select")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public Result list(@RequestBody Map<String,Object> map){
        return classesService.list(map);
    }


    @Log("添加班级")
    @RequiresPermissions("api-classes-add")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result save(@RequestBody Classes classes) throws Exception {
        return classesService.save(classes);
    }

    @Log("修改班级")
    @RequiresPermissions("api-classes-update")
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public Result update(@RequestBody Classes classes){
        return classesService.update(classes);
    }

    @Log("查询所有班级")
    @RequestMapping(value = "/findAll",method = RequestMethod.POST)
    public Result findAll(){
        return classesService.getClassesByCondition();
    }

    //作废班级
    @Log("作废班级")
    @RequestMapping(value = "/invalid",method = RequestMethod.PUT)
    public Result invalid(@RequestBody Map<String,Object> map){
        return classesService.invalid(map);
    }
}
