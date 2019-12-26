package com.access.business.access.service;

import com.access.business.access.repository.DepartmentRepository;
import com.access.business.access.repository.TeacherRepository;
import com.access.business.access.repository.UserRepository;
import com.teach.base.BaseService;
import com.teach.entity.access.Department;
import com.teach.entity.access.User;
import com.teach.entity.quality.transact.Teacher;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import com.teach.utils.IdWorker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DepartmentService extends BaseService {

    @Autowired
    private DepartmentRepository departmentRepository;

    //@Autowired
    //private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private IdWorker idWorker;

    public Result findAll() {
        return new Result(ResultCode.SUCCESS,departmentRepository.findAll(new Sort(Sort.Direction.DESC,"createTime")));
    }

    public Result saveDepartment(Department department) {
        department.setCreateTime(new Date());
        department.setId(idWorker.nextId() + "");
        department.setModifyId(currentUser().getId());
        department.setModifyUser(currentUser().getNickName());
        department.setModifyTime(new Date());
        departmentRepository.save(department);
        return Result.SUCCESS();
    }

    public Result updateDepartment(Department department) {
        Department target = departmentRepository.findById(department.getId()).get();
        BeanUtils.copyProperties(department,target);

        target.setModifyId(currentUser().getId());
        target.setModifyUser(currentUser().getNickName());
        target.setModifyTime(new Date());
        departmentRepository.save(target);

        return Result.SUCCESS();
    }


    public Result removeDepartment(String id) {

        List<Teacher> list = teacherRepository.findByDepartmentId(id);

        if(list != null && list.size() > 0) return new Result(ResultCode.DEPARTMENT_HAS_USERS);

        departmentRepository.deleteById(id);

        return Result.SUCCESS();
    }
}
