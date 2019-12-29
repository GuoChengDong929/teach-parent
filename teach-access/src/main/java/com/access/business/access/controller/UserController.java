package com.access.business.access.controller;

import com.access.business.access.service.UserService;
import com.teach.aop.Log;
import com.teach.entity.access.User;
import com.teach.entity.quality.transact.Teacher;
import com.teach.entity.vo.UserTeacherVo;
import com.teach.response.Result;
import com.teach.utils.BeanMapUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Log("用户列表")
    @RequiresPermissions("api-user-select")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public Result list(@RequestBody Map<String,Object> map){
        return userService.list(map);
    }

    @Log("添加用户")
    @RequiresPermissions("api-user-add")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result save(@RequestBody UserTeacherVo vo) throws Exception {

        User user = new User();
        Teacher teacher = new Teacher();

        BeanUtils.copyProperties(vo,user);
        BeanUtils.copyProperties(vo,teacher);

        return userService.save(user,teacher);
    }

    @Log("修改用户")
    @RequiresPermissions("api-user-update")
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public Result update(@RequestBody UserTeacherVo vo){
        User user = new User();
        Teacher teacher = new Teacher();

        BeanUtils.copyProperties(vo,user);
        BeanUtils.copyProperties(vo,teacher);

        return userService.update(user,teacher);

    }

    @Log("查询当前用户的信息")
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public Result findUserInfo(){
        return userService.findUserInfo();
    }

    @Log("修改密码")
    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST)
    public Result updatePassword(@RequestBody Map<String,Object> map){
        return userService.updatePassword(map);
    }
}
