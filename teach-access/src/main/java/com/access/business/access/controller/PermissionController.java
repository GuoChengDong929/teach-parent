package com.access.business.access.controller;

import com.access.business.access.service.PermissionService;
import com.teach.entity.access.Permission;
import com.teach.error.CommonException;
import com.teach.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/all")
    public Result all(){
        return permissionService.all();
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result savePermission(@RequestBody Permission permission) throws CommonException {
        return permissionService.savePermission(permission);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public Result updatePermission(@RequestBody Permission permission){
        return permissionService.updatePermission(permission);
    }

    @RequestMapping(value = "/remove/{id}",method = RequestMethod.DELETE)
    public Result removePermission(@PathVariable("id")String id){
        return permissionService.removePermission(id);
    }

}