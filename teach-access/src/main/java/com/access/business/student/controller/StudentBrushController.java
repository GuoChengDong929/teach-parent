package com.access.business.student.controller;

import com.access.business.student.service.StudentBrushService;
import com.teach.aop.Log;
import com.teach.error.CommonException;
import com.teach.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description:
 * @Author 韩雪松
 * @Date 2019/12/26
 **/
@RestController
@RequestMapping("/brush")
public class StudentBrushController {

    @Autowired
    private StudentBrushService studentBrushService;

    @Log("学生刷题:通过章节ID,获得章节下的所有试题")
    @RequestMapping(value = "/getQuestionsByChapter",method = RequestMethod.POST)
    public Result getQuestionsByChapter(@RequestBody Map<String,Object> map) throws CommonException {
        return studentBrushService.getQuestionsByChapter(map);
    }

    @Log("学生刷题:加入已答题")
    @RequestMapping(value = "/updateOk",method = RequestMethod.POST)
    public Result updateOk(@RequestBody Map<String,Object> map){
        return studentBrushService.updateOk(map);
    }

    @Log("学生刷题：去除已答题")
    @RequestMapping(value = "/removeOk",method = RequestMethod.POST)
    public Result removeOk(@RequestBody Map<String,Object> map){
        return studentBrushService.removeOk(map);
    }

}
