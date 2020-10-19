package com.access.business.access.service;

import com.access.business.access.repository.PermissionRepository;
import com.access.business.access.repository.RoleRepository;
import com.teach.base.BaseService;
import com.teach.entity.access.Permission;
import com.teach.entity.access.Role;
import com.teach.response.PageResult;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import com.teach.utils.IdWorker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@SuppressWarnings("all")
public class RoleService extends BaseService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;



    public Result list(Map<String, Object> map) {

        Pageable page = PageRequest.of(Integer.parseInt(map.get("page").toString()) - 1 , Integer.parseInt(map.get("size").toString()));

        Page<Role> result = roleRepository.findAll(page);

        PageResult<Role> pageResult = new PageResult<>(result.getTotalElements(),result.getContent());

        return new Result(ResultCode.SUCCESS,pageResult);
    }

    public Result saveRole(Role role) {

        role.setCreateTime(new Date());
        role.setId(idWorker.nextId() + "");
        role.setModifyId(currentUser().getId());
        role.setModifyTime(new Date());
        role.setModifyUser(currentUser().getNickName());


        Set<Permission> permissions = new HashSet<>();
        for(String permissionId : role.getPermissionIds().split(",")){
            Permission permission = permissionRepository.findById(permissionId).get();
            permissions.add(permission);
        }

        if(permissions != null && permissions.size() > 0){
            role.setPermissions(permissions);
            roleRepository.save(role);
            return Result.SUCCESS();
        }else {
            return Result.FAIL();
        }
    }

    public Result updateRole(Role role)  {

        Role target = roleRepository.findById(role.getId()).get();

        BeanUtils.copyProperties(role,target);

        target.setModifyUser(currentUser().getNickName());
        target.setModifyTime(new Date());
        target.setModifyId(currentUser().getId());


        //处理role和permission中间表关系gb数据
        String permissionIds = role.getPermissionIds();

        Set<Permission> permissions = new HashSet<>();
        for(String permissionId : permissionIds.split(",")){
            Permission permission = permissionRepository.findById(permissionId).get();
            permissions.add(permission);
        }

        if(permissions != null && permissions.size() > 0){
            target.setPermissions(permissions);
            roleRepository.save(target);
            return Result.SUCCESS();
        }else {
            return Result.FAIL();
        }

    }

    public Result getRoles() {
        return new Result(ResultCode.SUCCESS,roleRepository.findAll());
    }
}
