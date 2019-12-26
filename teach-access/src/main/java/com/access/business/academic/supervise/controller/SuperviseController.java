package com.access.business.academic.supervise.controller;

import com.access.business.academic.supervise.service.SuperviseService;
import com.teach.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Map;

/**
 * @Description:
 * @Author 韩雪松
 * @Date 2019/12/19
 **/
@RestController
@RequestMapping("/supervise")
public class SuperviseController {

    @Autowired
    private SuperviseService superviseService;

    //通过日期区间 班级id 来查看成绩监测-日测成绩的数据列表
    @RequestMapping(value = "/getScoresByDate",method = RequestMethod.POST)
    public Result getScoresByDate(@RequestBody Map<String,Object> map) throws ParseException {
        return superviseService.getScoresByDate(map);
    }

    //通过成绩监测-日测成绩 来查看学生个人的总体成绩列表
    @RequestMapping(value = "/getStudentScores",method = RequestMethod.POST)
    public Result getStudentScores(@RequestBody Map<String,Object> map) throws ParseException {
        return superviseService.getStudentScores(map);
    }
}
