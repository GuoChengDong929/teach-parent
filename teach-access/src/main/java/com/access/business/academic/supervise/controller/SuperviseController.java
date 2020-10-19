package com.access.business.academic.supervise.controller;

import com.access.business.academic.supervise.service.SuperviseService;
import com.teach.aop.Log;
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
    @Log("成绩监测:获取日测成绩")
    @RequestMapping(value = "/getScoresByDate",method = RequestMethod.POST)
    public Result getScoresByDate(@RequestBody Map<String,Object> map) throws ParseException {
        return superviseService.getScoresByDate(map);
    }


    @Log("成绩监测:获取周测成绩")
    @RequestMapping(value = "/getWeekScoresByDate",method = RequestMethod.POST)
    public Result getWeekScoresByDate(@RequestBody Map<String,Object> map) throws ParseException {
        return superviseService.getWeekScoresByDate(map);
    }

    @Log("成绩监测:获取月考成绩")
    @RequestMapping(value = "/getMonthScoresByDate",method = RequestMethod.POST)
    public Result getMonthScoresByDate(@RequestBody Map<String,Object> map) throws ParseException {
        return superviseService.getMonthScoresByDate(map);
    }


    //通过成绩监测-日测成绩 来查看学生个人的总体成绩列表
    @RequestMapping(value = "/getStudentScores",method = RequestMethod.POST)
    public Result getStudentScores(@RequestBody Map<String,Object> map) throws ParseException {
        return superviseService.getStudentScores(map);
    }


    @Log("成绩监测:月考考核对比")
    @RequestMapping(value = "/comparisonMonth",method = RequestMethod.POST)
    public Result comparisonMonth(@RequestBody Map<String,Object> map) throws ParseException {
        return superviseService.comparisonMonth(map);
    }

    @Log("成绩检测:日测考核对比")
    @RequestMapping(value = "/comparisonDay",method = RequestMethod.POST)
    public Result comparisonDay(@RequestBody Map<String,Object> map) throws ParseException {
        return superviseService.comparisonDay(map);
    }

    @Log("成绩检测:周测考核对比")
    @RequestMapping(value = "/comparisonWeek",method = RequestMethod.POST)
    public Result comparisonWeek(@RequestBody Map<String,Object> map) throws ParseException {
        return superviseService.comparisonWeek(map);
    }


}
