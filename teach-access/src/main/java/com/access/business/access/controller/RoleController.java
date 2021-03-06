package com.access.business.access.controller;

import com.access.business.access.service.RoleService;
import com.teach.aop.Log;
import com.teach.entity.access.Role;
import com.teach.response.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Log("查询角色集合")
    @RequiresPermissions("api-role-select")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public Result list(@RequestBody Map<String,Object> map){
        return roleService.list(map);
    }

    @Log("获取所有角色")
    @RequiresPermissions("api-role-permission-select")
    @RequestMapping(value = "/getRoles",method = RequestMethod.GET)
    public Result getRoles(){
        return roleService.getRoles();
    }

    @Log("添加角色")
    @RequiresPermissions("api-role-add")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result saveRole(@RequestBody Role role)  {
        return roleService.saveRole(role);
    }

    @Log("修改角色")
    @RequiresPermissions("api-role-update")
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public Result updateRole(@RequestBody Role role)  {
        return roleService.updateRole(role);
    }

}
