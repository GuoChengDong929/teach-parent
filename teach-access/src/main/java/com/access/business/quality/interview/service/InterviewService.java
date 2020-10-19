package com.access.business.quality.interview.service;


import com.access.business.access.repository.UserRepository;
import com.access.business.quality.interview.mapper.InterviewMapper;
import com.access.business.quality.student.mapper.StudentMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.teach.base.BaseService;
import com.teach.entity.access.User;
import com.teach.entity.quality.interview.Interview;
import com.teach.entity.quality.student.Student;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import com.teach.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class InterviewService extends BaseService {

    @Autowired
    private InterviewMapper interviewMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentMapper studentMapper;

    public Result findAll() {
        String currentUserId = currentUser().getId();

        User user = userRepository.findById(currentUserId).get();

        Map<String,Object> map = new HashMap<>();

        if("saasAdmin".equals(user.getLevel())){
            List<Interview> interviews = interviewMapper.selectList(null);

            this.createInfo(interviews);

            map.put("enVisible","1");
            map.put("interviews",interviews);
            return new Result(ResultCode.SUCCESS,map);
        }

        Date firstDayDateOfMonth = DateUtils.getFirstDayDateOfMonth(new Date());
        Date lastDayDateOfMonth = DateUtils.getLastDayOfMonth(new Date());

        QueryWrapper<Interview> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id",currentUserId);
        queryWrapper.between("create_time",firstDayDateOfMonth,lastDayDateOfMonth);
        queryWrapper.groupBy("student_id");

        List<Interview> interviews = interviewMapper.selectList(queryWrapper);
        this.createInfo(interviews);
        map.put("enVisible","0");
        map.put("interviews",interviews);
        return new Result(ResultCode.SUCCESS,map);
    }

    private void createInfo(List<Interview> list) {
        if(list != null && list.size() > 0){
            for(Interview interview : list){
                String studentId = interview.getStudentId();
                User target = userRepository.findById(studentId).get();
                if(target != null){
                    interview.setStudentName(target.getNickName());
                }
                Student student = studentMapper.selectById(studentId);
                if(student != null){
                    interview.setClassesName(student.getClassesName());
                }
            }
        }
    }


    public Result getInterviewInfo(Interview interview) {
        String studentId = interview.getStudentId();
        QueryWrapper<Interview> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id",studentId);
        queryWrapper.orderByDesc("create_time");
        List<Interview> interviews = interviewMapper.selectList(queryWrapper);
        return new Result(ResultCode.SUCCESS,interviews);
    }

    public Result saveInterview(Interview interview) {
        interview.setTeacherId(currentUser().getId());
        interview.setCreateTime(new Date());
        interviewMapper.insert(interview);
        return Result.SUCCESS();

    }

    public Result getListByInterview() {
       String currentId = currentUser().getId();
       User user = userRepository.findById(currentId).get();

       Map<String,Object> map = new HashMap<>();
       if("saasAdmin".equals(user.getLevel())){
           List<Interview> interviews = interviewMapper.selectList(null);
           this.createInfo(interviews);
           map.put("enVisible","1");
           map.put("interviews",interviews);
           return new Result(ResultCode.SUCCESS,map);
       }
        Date firstDayDateOfMonth = DateUtils.getFirstDayDateOfMonth(new Date());
        Date lastDayOfMonth = DateUtils.getLastDayOfMonth(new Date());

        QueryWrapper<Interview> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id",currentUser().getId());
        queryWrapper.notBetween("create_time",firstDayDateOfMonth,lastDayOfMonth);
        queryWrapper.groupBy("student_id");
        List<Interview> interviews = interviewMapper.selectList(queryWrapper);

        this.createInfo(interviews);
        map.put("enVisible","0");
        map.put("interviews",interviews);
        return new Result(ResultCode.SUCCESS,map);
    }
}
