package com.access.business.quality.transact.service;

import com.access.business.academic.curriculum.mapper.CurriculumMapper;
import com.access.business.access.repository.UserRepository;
import com.access.business.quality.student.mapper.StudentMapper;
import com.access.business.quality.teahcer.mapper.TeacherMapper;
import com.access.business.quality.transact.mapper.ClassesMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teach.base.BaseService;
import com.teach.entity.academic.curriculum.Curriculum;
import com.teach.entity.access.User;
import com.teach.entity.quality.student.Student;
import com.teach.entity.quality.transact.Classes;
import com.teach.entity.quality.transact.Teacher;
import com.teach.response.PageResult;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import com.teach.utils.IdWorker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@SuppressWarnings("all")
public class ClassesService extends BaseService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private ClassesMapper classesMapper;

    @Autowired
    private CurriculumMapper curriculumMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    public Result list(Map<String, Object> map) {

        Integer page = Integer.parseInt(map.get("page").toString());
        Integer size = Integer.parseInt(map.get("size").toString());

        IPage<Classes> iPage = new Page<>(page,size);

        QueryWrapper<Classes> queryWrapper = new QueryWrapper<>();

        queryWrapper.orderByDesc("create_time");

        IPage<Classes> result = classesMapper.selectPage(iPage, queryWrapper);


        List<Classes> list = result.getRecords();

        PageResult<Classes> pageResult = new PageResult<>(result.getTotal(),list);

        return new Result(ResultCode.SUCCESS,pageResult);
    }

    public Result save(Classes classes) throws Exception {


        classes.setId(idWorker.nextId() + "");
        classes.setCreateTime(new Date());
        classes.setPersonNumber(0);
        classes.setInvalid("1");

        Curriculum curriculum = curriculumMapper.selectById(classes.getCurriculumId());

        classes.setCurriculumName(curriculum.getName());


        String bzrId = classes.getBzrId();
        String jyId = classes.getJyId();
        String zjId = classes.getZjId();


        StringBuffer sb = new StringBuffer();

        if(jyId == null){
            sb.append("-");
        }else{
            User user = userRepository.findById(jyId).get();
            sb.append(user.getNickName());
        }

        sb.append(",");

        if(zjId == null){
            sb.append("-");
        }else{
            User user = userRepository.findById(zjId).get();
            sb.append(user.getNickName());
        }

        sb.append(",");

        if(bzrId == null){
            sb.append("-");
        }else{
            User user = userRepository.findById(bzrId).get();
            sb.append(user.getNickName());
        }

        classes.setTeachers(sb.toString());

        classes.setModifyId(currentUser().getId());
        classes.setModifyUser(currentUser().getNickName());
        classes.setModifyTime(new Date());

        classesMapper.insert(classes);

        return Result.SUCCESS();
    }

    public Result getClassesByCondition() {
        QueryWrapper<Classes> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("invalid","1");//查询不作废的班级
        List<Classes> classes = classesMapper.selectList(queryWrapper);

        return new Result(ResultCode.SUCCESS,classes);
    }

    public Result update(Classes classes) {


        Classes target = classesMapper.selectById(classes.getId());

        BeanUtils.copyProperties(classes,target);


        String bzrId = target.getBzrId();
        String jyId = target.getJyId();
        String zjId = target.getZjId();


        StringBuffer sb = new StringBuffer();

        if(jyId == null){
            sb.append("-");
        }else{
            User user = userRepository.findById(jyId).get();
            sb.append(user.getNickName());
        }

        sb.append(",");

        if(zjId == null){
            sb.append("-");
        }else{
            User user = userRepository.findById(zjId).get();
            sb.append(user.getNickName());
        }

        sb.append(",");

        if(bzrId == null){
            sb.append("-");
        }else{
            User user = userRepository.findById(bzrId).get();
            sb.append(user.getNickName());
        }

        target.setTeachers(sb.toString());


        Curriculum curriculum = curriculumMapper.selectById(target.getCurriculumId());

        target.setCurriculumName(curriculum.getName());


        target.setModifyId(currentUser().getId());
        target.setModifyUser(currentUser().getNickName());
        target.setModifyTime(new Date());

        classesMapper.updateById(target);

        return Result.SUCCESS();

    }


    //TODO: 只要有业务跟班级有关联, 在作废的时候需要把关联的业务统统处理掉,才能作废
    public Result invalid(Map<String, Object> map) {
        String classesId = map.get("classesId").toString();

        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("classes_id",classesId);

        List<Student> students = studentMapper.selectList(queryWrapper);

        if(students != null && students.size() > 0){
            return new Result(ResultCode.DO_NOT_INVALID_CLASSES);
        }

        Classes classes = classesMapper.selectById(classesId);
        classes.setInvalid("0");

        classes.setModifyId(currentUser().getId());
        classes.setModifyUser(currentUser().getNickName());
        classes.setModifyTime(new Date());
        classesMapper.updateById(classes);
        return Result.SUCCESS();

    }

    public Result getClassesListById() {
        String id = currentUser().getId();
        Teacher teacher = teacherMapper.selectById(id);
        String jobTitle = teacher.getJobTitle();

        QueryWrapper<Classes> queryWrapper = new QueryWrapper<>();
        if("教员".equals(jobTitle)){
            queryWrapper.eq("jy_id",id);
        }else if("班主任".equals(jobTitle)){
            queryWrapper.eq("bzr_id",id);
        }else {
            return new Result(ResultCode.Not_Classes_Id);
        }

        queryWrapper.orderByDesc("create_time");
        List<Classes> classes = classesMapper.selectList(queryWrapper);
        return new Result(ResultCode.SUCCESS,classes);


    }

    public Result getClassesName() {
        List<Classes> classes = classesMapper.selectList(null);
        return new Result(ResultCode.SUCCESS,classes);
    }
}
