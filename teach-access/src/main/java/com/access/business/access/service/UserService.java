package com.access.business.access.service;

import com.access.business.access.repository.RoleRepository;
import com.access.business.access.repository.TeacherRepository;
import com.access.business.access.repository.UserRepository;
import com.teach.entity.access.Role;
import com.teach.entity.access.User;
import com.teach.entity.quality.transact.Teacher;
import com.teach.entity.vo.UserTeacherVo;
import com.teach.response.PageResult;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import com.teach.utils.IdWorker;
import com.teach.utils.PinYinUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
@Transactional
@SuppressWarnings("all")
public class UserService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;


    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Result list(Map<String, Object> map) {

        Integer page = Integer.parseInt(map.get("page").toString());

        Integer size = Integer.parseInt(map.get("size").toString());

        Object departmentId = map.get("departmentId");


        //根据条件先查询teacher表,拿到teacher的id查询user表, 这样做的原因是需要获得roles


        Pageable pageable = PageRequest.of(page-1,size);

        Specification<Teacher> specification = new Specification<Teacher>() {
            @Override
            public Predicate toPredicate(Root<Teacher> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();

                list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class),departmentId.toString()));

                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };

        Page<Teacher> result = teacherRepository.findAll(specification, pageable);

        long total = result.getTotalElements();

        List<Teacher> teachers = result.getContent();


        List<UserTeacherVo> vos = new ArrayList<>();

        if(teachers != null && teachers.size() > 0){
            for (Teacher teacher : teachers) {
                String id = teacher.getId();
                User user = userRepository.findById(id).get();
                UserTeacherVo vo = new UserTeacherVo();
                BeanUtils.copyProperties(teacher,vo);
                BeanUtils.copyProperties(user,vo);
                vos.add(vo);
            }
        }

        PageResult<UserTeacherVo> pageResult = new PageResult<>(total,vos);


        return new Result(ResultCode.SUCCESS,pageResult);

    }

    public Result save(User user,Teacher teacher) {
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
            teacher.setId(id);
            teacherRepository.save(teacher);
            return Result.SUCCESS();
        }else{
            return Result.FAIL();
        }
    }

    public Result update(User user,Teacher teacher) {
        User target = userRepository.findById(user.getId()).get();
        BeanUtils.copyProperties(user,target);


        Teacher teacherTarget = teacherRepository.findById(user.getId()).get();

        BeanUtils.copyProperties(teacher,teacherTarget);


        Set<Role> roles = new HashSet<>();
        for (String roleId : user.getRoleIds().split(",")) {
            Role role = roleRepository.findById(roleId).get();
            roles.add(role);
        }

        if(roles != null && roles.size() > 0){
            target.setRoles(roles);
            userRepository.save(target);
            teacherRepository.save(teacherTarget);
            return Result.SUCCESS();
        }else {
            return Result.FAIL();
        }

    }
}
