package com.access.business.quality.teahcer.controller;

import com.access.business.quality.teahcer.service.TeacherService;
import com.teach.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    //查询班级列表中显示的教员,助教,班主任的数据集合
    @RequestMapping(value = "/findTeachersByJobTitle",method = RequestMethod.GET)
    public Result findTeachersByJobTitle(){
        return teacherService.findTeachersByJobTitle();
    }
}
