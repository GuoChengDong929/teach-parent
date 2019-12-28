package com.access.business.quality.student.controller;

import com.access.business.quality.student.service.StudentService;
import com.teach.entity.vo.UserStudentVo;
import com.teach.error.CommonException;
import com.teach.response.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

    @RequiresPermissions("api-student-select")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public Result list(@RequestBody Map<String,Object> map){
        return studentService.list(map);
    }

    @RequiresPermissions("api-student-add")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result save(@RequestBody UserStudentVo vo) throws CommonException {
        return studentService.save(vo);
    }

    @RequiresPermissions("api-student-update")
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public Result update(@RequestBody UserStudentVo vo) throws CommonException {
        return studentService.update(vo);
    }
}
