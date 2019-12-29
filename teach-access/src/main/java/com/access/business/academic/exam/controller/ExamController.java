package com.access.business.academic.exam.controller;

import com.access.business.academic.exam.service.ExamService;
import com.teach.aop.Log;
import com.teach.entity.academic.exam.Exam;
import com.teach.response.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    @Log("教师端:试题列表")
    @RequestMapping(value = "/getExamList",method = RequestMethod.POST)
    public Result list(@RequestBody Map<String,Object> map){
        return examService.list(map);
    }

    @Log("教师端:保存试题")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result save(@RequestBody Map<String,Object> map) throws Exception {
        return examService.save(map);
    }

    @Log("教师端:开始考试")
    @RequestMapping(value = "/start",method = RequestMethod.POST)
    public Result start(@RequestBody Exam exam){
        return examService.start(exam);
    }

    /**开始考试前,确认考试状态没被定时器修改*/
    @Log("教师端:考试前确认考试状态没有被定时器修改")
    @RequestMapping(value = "/findById/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable("id")String id){
        return examService.findById(id);
    }

    /**查看试卷(老师),考试试卷(学生) */
    @Log("教师端:学生端:查看试卷")
    @RequestMapping(value = "/findExamByCache/{id}",method = RequestMethod.GET)
    public Result findExamByCache(@PathVariable("id")String id){
        return examService.findExamByCache(id);
    }

    /**试卷讲解,带答案*/
    @Log("教师端:试卷讲解,带答案")
    @RequestMapping(value = "/findExamByDBHasAnswer",method = RequestMethod.POST)
    public Result findExamByDBHasAnswer(@RequestBody Exam exam){
        return examService.findExamByDBHasAnswer(exam);
    }

    @Log("教师端:通过试卷ID查询当前试卷下所有学生的成绩")
    @RequestMapping(value = "/getStudentListByExam",method = RequestMethod.POST)
    public Result getStudentListByExam(@RequestBody Exam exam){
        return examService.getStudentListByExam(exam);
    }

    //停止考试
    @RequestMapping(value = "/stop",method = RequestMethod.POST)
    public Result stop(@RequestBody Exam exam){
        return examService.stop(exam);
    }

    //试卷分析,图表数据
    @RequestMapping(value = "/analysisExam",method = RequestMethod.POST)
    public Result analysisExam(@RequestBody Exam exam){
        return examService.analysisExam(exam);
    }

    /**获得当前试卷的所有学生的所有问答题*/
    @RequestMapping(value = "/markingStudentAsks",method = RequestMethod.POST)
    public Result markingStudentAsks(@RequestBody Exam exam){
        return examService.markingStudentAsks(exam);
    }


    /**查询某个学生的某个单选题是否已经评分*/
    @RequestMapping(value = "/getStudentAskResult",method = RequestMethod.POST)
    public Result getStudentAskResult(@RequestBody Map<String,Object> map){
        return examService.getStudentAskResult(map);
    }

    /**给某个学生的某个单选题评分*/
    @RequestMapping(value = "/updateStudentAskScore",method = RequestMethod.POST)
    public Result updateStudentAskScore(@RequestBody Map<String,Object> map){
        return examService.updateStudentAskScore(map);
    }

    @RequestMapping(value = "/endExam",method = RequestMethod.POST)
    public Result endExam(@RequestBody Exam exam){
        return examService.endExam(exam);
    }
}
