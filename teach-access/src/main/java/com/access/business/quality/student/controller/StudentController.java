package com.access.business.quality.student.controller;

import com.access.business.quality.student.service.StudentService;
import com.teach.entity.vo.UserStudentVo;
import com.teach.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {


    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public Result list(@RequestBody Map<String,Object> map){
        return studentService.list(map);
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result save(@RequestBody UserStudentVo vo){
        return studentService.save(vo);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public Result update(@RequestBody UserStudentVo vo){
        return studentService.update(vo);
    }
}
