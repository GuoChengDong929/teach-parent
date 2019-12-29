package com.access.business.access.controller;

import com.access.business.access.service.PermissionService;
import com.teach.aop.Log;
import com.teach.entity.access.Permission;
import com.teach.error.CommonException;
import com.teach.response.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;


    @Log("递归查询所有权限集合")
    @RequiresPermissions("api-permission-select")
    @RequestMapping("/all")
    public Result all(){
        return permissionService.all();
    }

    @Log("添加权限")
    @RequiresPermissions("api-permission-add")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result savePermission(@RequestBody Permission permission) throws CommonException {
        return permissionService.savePermission(permission);
    }

    @Log("修改权限")
    @RequiresPermissions("api-permission-update")
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public Result updatePermission(@RequestBody Permission permission){
        return permissionService.updatePermission(permission);
    }

    @Log("删除权限")
    @RequiresPermissions("api-permission-delete")
    @RequestMapping(value = "/remove/{id}",method = RequestMethod.DELETE)
    public Result removePermission(@PathVariable("id")String id){
        return permissionService.removePermission(id);
    }

}
