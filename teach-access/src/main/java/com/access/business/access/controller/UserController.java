package com.access.business.access.controller;

import com.access.business.access.service.UserService;
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

    @RequiresPermissions("api-user-select")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public Result list(@RequestBody Map<String,Object> map){
        return userService.list(map);
    }

    @RequiresPermissions("api-user-add")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result save(@RequestBody UserTeacherVo vo) throws Exception {

        User user = new User();
        Teacher teacher = new Teacher();

        BeanUtils.copyProperties(vo,user);
        BeanUtils.copyProperties(vo,teacher);

        return userService.save(user,teacher);
    }

    @RequiresPermissions("api-user-update")
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public Result update(@RequestBody UserTeacherVo vo){
        User user = new User();
        Teacher teacher = new Teacher();

        BeanUtils.copyProperties(vo,user);
        BeanUtils.copyProperties(vo,teacher);

        return userService.update(user,teacher);

    }


    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public Result findUserInfo(){
        return userService.findUserInfo();
    }

    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST)
    public Result updatePassword(@RequestBody Map<String,Object> map){
        return userService.updatePassword(map);
    }
}
