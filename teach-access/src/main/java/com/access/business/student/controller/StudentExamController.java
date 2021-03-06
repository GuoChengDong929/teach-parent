package com.access.business.student.controller;

import com.access.business.student.service.StudentExamService;
import com.teach.aop.Log;
import com.teach.entity.academic.exam.Exam;
import com.teach.error.CommonException;
import com.teach.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description:
 * @Author 韩雪松
 * @Date 2019/12/12
 **/
@RestController
@RequestMapping("/student/exam")
public class StudentExamController {

    @Autowired
    private StudentExamService studentExamService;

    @Log("学生端:试卷列表")
    @RequestMapping(value = "/getList",method = RequestMethod.POST)
    public Result getList(@RequestBody Map<String,Object> map){
        return studentExamService.getList(map);
    }

    //学生提交试卷
    @Log("学生端:提交试卷")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result save(@RequestBody Map<String,Object>map){
        return studentExamService.save(map);
    }

    //当学生点击开始考试按钮时,查看学生的试卷是否是2,如果是2,则不让再次进入考试
    @Log("学生端:点击考试时校验是否是2")
    @RequestMapping(value = "/getScoreStatus/{id}",method = RequestMethod.GET)
    public Result getScoreStatus(@PathVariable("id") String id) throws CommonException {
        return studentExamService.getScoreStatus(id);
    }

    //获取试卷的考试状态, 如果查询到当前试卷的状态为4,则停止考试
    @Log("学生端:获得试卷状态是否是4,则停止考试,强制交卷")
    @RequestMapping(value = "/getExamStatus/{id}",method = RequestMethod.GET)
    public Result getExamStatus(@PathVariable("id") String id) throws CommonException {
        return studentExamService.getExamStatus(id);
    }

    //学生查看试卷:  如果当前学生的score中的status不是2, 试卷状态不是为4,则不允许
    @Log("学生端:防止考试中时的BUG攻击")
    @RequestMapping(value = "/getShowExamStatus/{id}",method = RequestMethod.GET)
    public Result getShowExamStatus(@PathVariable("id") String id){
        return studentExamService.getShowExamStatus(id);
    }

    //保存临时试卷答案到缓存
    @Log("学生端:保存临时试卷答案到缓存")
    @RequestMapping(value = "/saveTempAnswer",method = RequestMethod.POST)
    public Result saveTempAnswer(@RequestBody Map<String,Object>map) throws Exception {
        return studentExamService.saveTempAnswer(map);
    }

    //回显考试时的临时数据
    @Log("学生端:回显考试时的临时数据")
    @RequestMapping(value = "/echoTempAnswer/{id}",method = RequestMethod.GET)
    public Result echoTempAnswer(@PathVariable("id")String id){
        return studentExamService.echoTempAnswer(id);
    }


    /**试卷讲解,带答案*/
    @Log("学生端:试卷讲解,带答案")
    @RequestMapping(value = "/findExamByDBHasAnswer",method = RequestMethod.POST)
    public Result findExamByDBHasAnswer(@RequestBody Exam exam){
        return studentExamService.findExamByDBHasAnswer(exam);
    }

    /** 试卷讲解时,学生数据的回显 */
    @Log("学生端:试卷讲解时,学生数据的回显")
    @RequestMapping(value = "/goBackStudentExamData",method = RequestMethod.POST)
    public Result goBackStudentExamData(@RequestBody Exam exam){
        return studentExamService.goBackStudentExamData(exam);
    }

    /**成绩分析*/
    @Log("学生端:成绩分析")
    @RequestMapping(value = "/analysisExam",method = RequestMethod.POST)
    public Result analysisExam(@RequestBody Exam exam){
        return studentExamService.analysisExam(exam);
    }

}
