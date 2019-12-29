package com.access.business.access.controller;

import com.access.business.access.service.DepartmentService;
import com.teach.aop.Log;
import com.teach.entity.access.Department;
import com.teach.response.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Log("查询所有部门集合")
    @RequiresPermissions("api-department-select")
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public Result findAll(){
        return departmentService.findAll();
    }

    @Log("添加部门")
    @RequiresPermissions("api-department-add")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result saveDepartment(@RequestBody Department department){
        return departmentService.saveDepartment(department);
    }

    @Log("修改部门")
    @RequiresPermissions("api-department-update")
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public Result updateDepartment(@RequestBody Department department){
        return departmentService.updateDepartment(department);
    }

    @Log("删除部门")
    @RequiresPermissions("api-department-delete")
    @RequestMapping(value = "/remove/{id}",method = RequestMethod.DELETE)
    public Result removeDepartment(@PathVariable("id")String id){
        return departmentService.removeDepartment(id);
    }
}
