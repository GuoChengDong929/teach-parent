package com.access.business.quality.student.service;

import com.access.business.academic.exam.mapper.ExamMapper;
import com.access.business.access.repository.RoleRepository;
import com.access.business.access.repository.UserRepository;
import com.access.business.quality.student.mapper.StudentMapper;
import com.access.business.quality.transact.mapper.ClassesMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teach.base.BaseService;
import com.teach.entity.academic.exam.Exam;
import com.teach.entity.access.Role;
import com.teach.entity.access.User;
import com.teach.entity.quality.student.Student;
import com.teach.entity.quality.transact.Classes;
import com.teach.entity.vo.UserStudentVo;
import com.teach.error.CommonException;
import com.teach.response.PageResult;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import com.teach.utils.IdWorker;
import com.teach.utils.PinYinUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
@Transactional
@SuppressWarnings("all")
public class StudentService extends BaseService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private ClassesMapper classesMapper;

    public Result list(Map<String, Object> map) {

        //防止用户不选择班级,直接通过输入学生姓名进行查询
        if(map.get("classesId") == null) return Result.FAIL();

        Integer page = Integer.parseInt(map.get("page").toString());
        Integer size = Integer.parseInt(map.get("size").toString());

        String classesId = map.get("classesId").toString();



        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("classes_id",classesId);

        Object nickName = map.get("nickName");

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

        if(nickName != null){
            List<UserStudentVo> list = new ArrayList<>();
            for (UserStudentVo vo : vos) {
                if(vo.getNickName().contains(nickName.toString())){
                    list.add(vo);
                }
            }

            PageResult<UserStudentVo> pageResult = new PageResult<>(total,list);
            return new Result(ResultCode.SUCCESS,pageResult);
        }


        PageResult<UserStudentVo> pageResult = new PageResult<>(total,vos);
        return new Result(ResultCode.SUCCESS,pageResult);



    }

    public Result save(UserStudentVo vo) throws CommonException {

        String classesId = vo.getClassesId();
        //通过班级id查询班级所有的试卷
        QueryWrapper<Exam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("classes_id",classesId);
        List<Exam> exams = examMapper.selectList(queryWrapper);

        if(exams != null && exams.size() > 0){
            for (Exam exam : exams) {
                String examStatus = exam.getExamStatus();
                if(!"4".equals(examStatus)){
                    throw new CommonException(ResultCode.INSERT_STUDENT_ERROR_IS_CLASSES_HAS_EXAMS_ING);
                }
            }
        }

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


            //String classesId = student.getClassesId();
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

    public Result update(UserStudentVo vo) throws CommonException {

        User user = new User();
        Student student = new Student();
        BeanUtils.copyProperties(vo,user);
        BeanUtils.copyProperties(vo,student);

        //查看当前修改的班级是否有未结束的试卷,如果有,则不让添加
        String newClassesId = vo.getClassesId();

        QueryWrapper<Exam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("classes_id",newClassesId);
        List<Exam> exams = examMapper.selectList(queryWrapper);

        if(exams != null && exams.size() > 0){
            for (Exam exam : exams) {
                String examStatus = exam.getExamStatus();
                if(!"4".equals(examStatus)){
                    throw new CommonException(ResultCode.INSERT_STUDENT_ERROR_IS_CLASSES_HAS_EXAMS_ING);
                }
            }
        }



        Student studentTarget = studentMapper.selectById(user.getId());
        User userTarget = userRepository.findById(user.getId()).get();


        String classesId = student.getClassesId();





        BeanUtils.copyProperties(student,studentTarget);
        BeanUtils.copyProperties(user,userTarget);

        Classes classes = classesMapper.selectById(classesId);
        String classesName = classes.getClassesName();

        studentTarget.setClassesName(classesName);

        Set<Role> roles = new HashSet<>();
        for (String roleId : user.getRoleIds().split(",")) {
            Role role = roleRepository.findById(roleId).get();
            roles.add(role);
        }

        if(roles != null && roles.size() > 0){
            userTarget.setRoles(roles);
            userTarget.setModifyId(currentUser().getId());
            userTarget.setModifyUser(currentUser().getNickName());
            userTarget.setModifyTime(new Date());
            userRepository.save(userTarget);
            studentMapper.updateById(studentTarget);
            return Result.SUCCESS();
        }else {
            return Result.FAIL();
        }

    }
}
