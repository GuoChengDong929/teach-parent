package com.access.business.quality.student.service;

import com.access.business.access.repository.RoleRepository;
import com.access.business.access.repository.UserRepository;
import com.access.business.quality.student.mapper.StudentMapper;
import com.access.business.quality.transact.mapper.ClassesMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teach.entity.access.Role;
import com.teach.entity.access.User;
import com.teach.entity.quality.student.Student;
import com.teach.entity.quality.transact.Classes;
import com.teach.entity.vo.UserStudentVo;
import com.teach.response.PageResult;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import com.teach.utils.IdWorker;
import com.teach.utils.PinYinUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@SuppressWarnings("all")
public class StudentService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private ClassesMapper classesMapper;

    public Result list(Map<String, Object> map) {
        Integer page = Integer.parseInt(map.get("page").toString());
        Integer size = Integer.parseInt(map.get("size").toString());

        String classesId = map.get("classesId").toString();

        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("classes_id",classesId);


        IPage<Student> iPage = new Page<>(page,size);


        IPage<Student> result = studentMapper.selectPage(iPage, queryWrapper);
        long total = result.getTotal();
        List<Student> students = result.getRecords();


        List<UserStudentVo> vos = new ArrayList<>();

        if(students != null && students.size() > 0){
            for (Student student : students) {
                String id = student.getId();
                User user = userRepository.findById(id).get();

                UserStudentVo vo = new UserStudentVo();

                BeanUtils.copyProperties(student,vo);
                BeanUtils.copyProperties(user,vo);
                vos.add(vo);
            }
        }

        PageResult<UserStudentVo> pageResult = new PageResult<>(total,vos);


        return new Result(ResultCode.SUCCESS,pageResult);

    }

    public Result save(UserStudentVo vo) {

        User user = new User();
        Student student = new Student();

        BeanUtils.copyProperties(vo,user);
        BeanUtils.copyProperties(vo,student);

        String id = idWorker.nextId() + "";
        user.setId(id);
        String username = PinYinUtil.toPinyin(user.getNickName()) + (int)(Math.random()*8999);
        user.setUsername(username);
        user.setLevel("user");

        user.setStatus("1");
        user.setPassword(new Md5Hash("123456",username,3).toString()); //参数一: 加密谁?   混淆字符串1234 : 加盐  3 加密次数


        //处理用户与角色的关联关系
        Set<Role> roles = new HashSet<>();
        for (String roleId : user.getRoleIds().split(",")) {
            Role role = roleRepository.findById(roleId).get();
            roles.add(role);
        }

        if(roles != null && roles.size() > 0){
            user.setRoles(roles);
            userRepository.save(user);


            String classesId = student.getClassesId();
            Classes classes = classesMapper.selectById(classesId);
            student.setClassesName(classes.getClassesName());

            student.setStudyType("1"); //1 在读  2休学 3 退学 4毕业

            student.setId(id);
            studentMapper.insert(student);

            return Result.SUCCESS();
        }else{
            return Result.FAIL();
        }

    }

    public Result update(UserStudentVo vo) {
        User user = new User();
        Student student = new Student();
        BeanUtils.copyProperties(vo,user);
        BeanUtils.copyProperties(vo,student);

        Student studentTarget = studentMapper.selectById(user.getId());
        User userTarget = userRepository.findById(user.getId()).get();

        BeanUtils.copyProperties(student,studentTarget);
        BeanUtils.copyProperties(user,userTarget);

        Set<Role> roles = new HashSet<>();
        for (String roleId : user.getRoleIds().split(",")) {
            Role role = roleRepository.findById(roleId).get();
            roles.add(role);
        }

        if(roles != null && roles.size() > 0){
            userTarget.setRoles(roles);
            userRepository.save(userTarget);
            studentMapper.updateById(studentTarget);
            return Result.SUCCESS();
        }else {
            return Result.FAIL();
        }

    }
}
