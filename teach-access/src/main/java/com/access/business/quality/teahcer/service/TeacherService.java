package com.access.business.quality.teahcer.service;

import com.access.business.access.repository.UserRepository;
import com.access.business.quality.teahcer.mapper.TeacherMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.teach.entity.access.User;
import com.teach.entity.quality.transact.Teacher;
import com.teach.entity.vo.TeacherVo;
import com.teach.entity.vo.UserTeacherVo;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@SuppressWarnings("all")
public class TeacherService {


    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private UserRepository userRepository;

    public Result findAll(Map<String, Object> map) {

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();

        if(map.get("jobTitle") != null){
            queryWrapper.eq("jobTitle",map.get("jobTitle").toString());
        }

        queryWrapper.orderByDesc();

        List<Teacher> teachers = teacherMapper.selectList(queryWrapper);

        List<UserTeacherVo> list = new ArrayList<>();

        if(teachers != null && teachers.size() > 0){
            for (Teacher teacher : teachers) {
                String id = teacher.getId();
                User user = userRepository.findById(id).get();
                UserTeacherVo vo = new UserTeacherVo();
                BeanUtils.copyProperties(user,vo);
                BeanUtils.copyProperties(teacher,vo);
                list.add(vo);
            }
        }

        return new Result(ResultCode.SUCCESS,list);

    }

    public Result findTeachersByJobTitle() {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("job_title","教员").or().eq("job_title","班主任").or().eq("job_title","助教");
        queryWrapper.orderByDesc("id");

        List<Teacher> teachers = teacherMapper.selectList(queryWrapper);


        TeacherVo teacherVo = new TeacherVo();
        List<UserTeacherVo> zjTeacher = new ArrayList<>();
        List<UserTeacherVo> jyTeacher = new ArrayList<>();
        List<UserTeacherVo> bzrTeacher = new ArrayList<>();

        if(teachers != null && teachers.size() > 0){

            for (Teacher teacher : teachers) {
                String id = null;
                User user = null;
                if(teacher.getJobTitle().equals("助教")){
                    id = teacher.getId();
                    user = userRepository.findById(id).get();

                    UserTeacherVo vo = new UserTeacherVo();
                    BeanUtils.copyProperties(teacher,vo);
                    BeanUtils.copyProperties(user,vo);

                    zjTeacher.add(vo);


                }else if ("教员".equals(teacher.getJobTitle())){
                    id = teacher.getId();
                    user = userRepository.findById(id).get();

                    UserTeacherVo vo = new UserTeacherVo();
                    BeanUtils.copyProperties(teacher,vo);
                    BeanUtils.copyProperties(user,vo);

                    jyTeacher.add(vo);

                }else if("班主任".equals(teacher.getJobTitle())){
                    id = teacher.getId();
                    user = userRepository.findById(id).get();

                    UserTeacherVo vo = new UserTeacherVo();
                    BeanUtils.copyProperties(teacher,vo);
                    BeanUtils.copyProperties(user,vo);

                    bzrTeacher.add(vo);
                }
            }

            teacherVo.setBzrTeacher(bzrTeacher);
            teacherVo.setJyTeacher(jyTeacher);
            teacherVo.setZjTeacher(zjTeacher);
        }

        return new Result(ResultCode.SUCCESS,teacherVo);
    }
}
