package com.access.business.academic.supervise.service;

import com.access.business.academic.exam.mapper.ExamMapper;
import com.access.business.academic.exam.mapper.ScoreMapper;
import com.access.business.access.repository.UserRepository;
import com.access.business.quality.student.mapper.StudentMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.teach.entity.academic.exam.Exam;
import com.teach.entity.academic.exam.Score;
import com.teach.entity.access.User;
import com.teach.entity.quality.student.Student;
import com.teach.entity.vo.StudentScoreVo;
import com.teach.entity.vo.StudentVo;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author 韩雪松
 * @Date 2019/12/19
 **/
@Service
@Transactional
public class SuperviseService {

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private UserRepository userRepository;

    public Result getScoresByDate(Map<String, Object> map) throws ParseException {


        StudentScoreVo vo = new StudentScoreVo();

        String classesId = map.get("classesId").toString();

        String[] str = map.get("date").toString().split("~");

        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(str[0]);
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(str[1]);

        //查询班级这个月所有的考试,获得examTime 用于table表格的表头

        QueryWrapper<Exam> examQueryWrapper = new QueryWrapper<>();

        examQueryWrapper.eq("classes_id",classesId);
        examQueryWrapper.between("exam_time",startDate,endDate);

        List<Exam> exams = examMapper.selectList(examQueryWrapper);

        List<String> headers = new ArrayList<>();

        if(exams != null && exams.size() > 0){
            for (Exam exam : exams) {
                String format = new SimpleDateFormat("yyyy-MM-dd").format(exam.getExamTime());
                headers.add(format);
            }
        }

        vo.setHeaders(headers);


        List<StudentVo> studentVos = new ArrayList<>();


        //查询班级所有学生
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("classes_id",classesId);
        List<Student> students = studentMapper.selectList(studentQueryWrapper);

        //获得单个学生 这个时期的所有成绩



        if(students != null && students.size() > 0){
            for (Student student : students) {

                User user = userRepository.findById(student.getId()).get();

                StudentVo studentVo = new StudentVo();

                studentVo.setStudentId(student.getId());
                studentVo.setNickName(user.getNickName());


                List<Integer> resultScores = new ArrayList<>();

                for (Exam exam : exams) {
                    String studentId = student.getId();
                    String examId = exam.getId();
                    QueryWrapper<Score> scoreQueryWrapper = new QueryWrapper<>();
                    scoreQueryWrapper.eq("student_id",studentId);
                    scoreQueryWrapper.eq("exam_id",examId);

                    List<Score> scores = scoreMapper.selectList(scoreQueryWrapper);

                    if(scores != null && scores.size() > 0){
                        for (Score score : scores) {
                            resultScores.add(score.getScore());
                        }
                    }
                }

                studentVo.setScores(resultScores);
                studentVos.add(studentVo);
            }

        }
        vo.setVos(studentVos);


        return new Result(ResultCode.SUCCESS,vo);
    }
}