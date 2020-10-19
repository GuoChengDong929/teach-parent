package com.access.business.quality.interview.controller;

import com.access.business.quality.interview.service.InterviewService;
import com.access.business.quality.student.service.StudentService;
import com.teach.aop.Log;
import com.teach.entity.quality.interview.Interview;
import com.teach.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/interview")
public class InterviewController {

    @Autowired
    private InterviewService interviewService;

    @Autowired
    private StudentService studentService;

    @Log("查询访谈记录")
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public Result findAll(){
        return interviewService.findAll();
    }

    @RequestMapping(value = "/getInterviewInfo",method = RequestMethod.POST)
    public Result getInterviewInfo(@RequestBody Interview interview){
        return interviewService.getInterviewInfo(interview);
    }

    @RequestMapping(value = "/saveInterview",method = RequestMethod.POST)
    public Result saveInterview(@RequestBody Interview interview){
        return interviewService.saveInterview(interview);
    }

    @RequestMapping(value = "/getListByInterview",method = RequestMethod.GET)
    public Result getListByInterview(){
        return interviewService.getListByInterview();
    }

    @RequestMapping(value = "/getStudent", method = RequestMethod.POST)
    public Result getStudent(@RequestBody Map<String, Object> map) {
        return studentService.getStudent(map);
    }
}
