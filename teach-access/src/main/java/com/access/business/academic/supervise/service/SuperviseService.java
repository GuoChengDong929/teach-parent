package com.access.business.academic.supervise.service;

import com.access.business.academic.exam.mapper.ExamMapper;
import com.access.business.academic.exam.mapper.ScoreMapper;
import com.access.business.access.repository.UserRepository;
import com.access.business.quality.student.mapper.StudentMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.teach.entity.academic.exam.Exam;
import com.teach.entity.academic.exam.Score;
import com.teach.entity.access.User;
import com.teach.entity.quality.student.Student;
import com.teach.entity.vo.ScoreVo;
import com.teach.entity.vo.StudentScoreInfoVo;
import com.teach.entity.vo.StudentScoreVo;
import com.teach.entity.vo.StudentVo;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import com.teach.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

        String classesId = map.get("classesId").toString();

        List<String> date = (List<String>) map.get("date");
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(date.get(0));
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(date.get(1));

        //查询班级这个月所有的考试,获得examTime 用于table表格的表头
        QueryWrapper<Exam> examQueryWrapper = new QueryWrapper<>();
        examQueryWrapper.eq("classes_id",classesId);
        examQueryWrapper.between("exam_time",startDate,endDate);
        examQueryWrapper.eq("exam_type","1");
        examQueryWrapper.orderByAsc("exam_time");
        List<Exam> exams = examMapper.selectList(examQueryWrapper);

        //处理表头
        List<String> headers = new ArrayList<>();
        if(exams != null && exams.size() > 0){
            for (Exam exam : exams) {
                Date examTime = exam.getExamTime();
                int day = DateUtils.getDateOfDay(examTime);
                headers.add(day + "日");
            }
        }

        List<StudentVo> studentVos = new ArrayList<>();
        
        //查询班级所有学生
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("classes_id",classesId);
        List<Student> students = studentMapper.selectList(studentQueryWrapper);
        
        //获得单个学生 日期范围内的所有成绩
        if(students != null && students.size() > 0){
            for (Student student : students) {
                User user = userRepository.findById(student.getId()).get();
                StudentVo studentVo = new StudentVo();

                studentVo.setStudentId(student.getId());
                studentVo.setNickName(user.getNickName());

                List<Integer> resultScores = new ArrayList<>();

                for (Exam exam : exams) {
                    studentVo.setExamId(exam.getId());
                    String studentId = student.getId();
                    String examId = exam.getId();

                    QueryWrapper<Score> scoreQueryWrapper = new QueryWrapper<>();
                    scoreQueryWrapper.eq("student_id",studentId);
                    scoreQueryWrapper.eq("exam_id",examId);

                    List<Score> scores = scoreMapper.selectList(scoreQueryWrapper);

                    if(scores != null && scores.size() > 0){
                        for (Score score : scores) {
                            studentVo.setScoreId(score.getId());
                            if("0".equals(score.getStatus())){ //未参考
                                resultScores.add(-1);
                            }else{
                                resultScores.add(score.getScore());
                            }
                        }
                    }
                }

                //求平均分
                int size = resultScores.size();

                if(size == 0){
                    studentVo.setAvg(0D);
                }else{
                    int sum = 0;
                    for (Integer resultScore : resultScores) {
                        if(resultScore == -1){
                            continue;
                        }else{
                            sum += resultScore;
                        }
                    }
                    int avg = sum / size;
                    studentVo.setAvg((double) avg);
                }

                studentVo.setScores(resultScores);
                studentVos.add(studentVo);
            }
        }

        StudentScoreVo vo = new StudentScoreVo();
        vo.setHeaders(headers);
        vo.setVos(studentVos);

        return new Result(ResultCode.SUCCESS,vo);
    }


    public Result getStudentScores(Map<String, Object> map) throws ParseException {
        String studentId = map.get("studentId").toString();
        String classesId = map.get("classesId").toString();
        String type= map.get("type").toString();


        String[] str = map.get("date").toString().split("~");

        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(str[0]);
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(str[1]);

        //通过班级id和学生id查询当前学生的所有试卷
        QueryWrapper<Exam> examQueryWrapper = new QueryWrapper<>();
        examQueryWrapper.eq("classes_id",classesId);
        examQueryWrapper.eq("exam_type",type);
        examQueryWrapper.between("exam_time",startDate,endDate);
        examQueryWrapper.orderByAsc("exam_time");

        List<Exam> exams = examMapper.selectList(examQueryWrapper);

        List<StudentScoreInfoVo> vos = new ArrayList<>();

        if(exams != null && exams.size() > 0){
            for (Exam exam : exams) {
                String examId = exam.getId();

                StudentScoreInfoVo vo = new StudentScoreInfoVo();

                QueryWrapper<Score> scoreQueryWrapper = new QueryWrapper<>();
                scoreQueryWrapper.eq("student_id",studentId);
                scoreQueryWrapper.eq("exam_id",examId);
                Score score = scoreMapper.selectOne(scoreQueryWrapper);
                vo.setExamTime(exam.getExamTime());
                String status = score.getStatus();
                if("0".equals(status)){
                    vo.setScore(-1);
                }else{
                    vo.setScore(score.getScore());
                }
                vos.add(vo);
            }
        }

        return new Result(ResultCode.SUCCESS,vos);
    }
}
