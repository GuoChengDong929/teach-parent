package com.access.config;


import com.access.business.access.service.PermissionService;
import com.access.business.access.service.UserService;
import com.teach.entity.access.Permission;
import com.teach.entity.access.User;
import com.teach.response.ProfileResult;
import com.teach.shiro.ParentRealm;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

public class AccessRealm extends ParentRealm {


    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    //认证方法 登录时触发
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.获取用户的手机号和密码
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        String username = upToken.getUsername();
        String password = new String( upToken.getPassword());
        //2.根据用户名
        User user = userService.findByUsername(username);
        //3.判断用户是否存在，用户密码是否和输入密码一致
        if(user != null && user.getPassword().equals(password)) {
            //4.构造安全数据并返回（安全数据：用户基本数据，权限信息 profileResult）
            ProfileResult result = null;
            if("user".equals(user.getLevel())) {
                result = new ProfileResult(user);
            }else {
                Map map = new HashMap();
                if("coAdmin".equals(user.getLevel())) {
                    map.put("enVisible","1");
                }
                List<Permission> list = permissionService.findAll(map);
                result = new ProfileResult(user,list);
            }
            //构造方法：安全数据，密码，realm域名
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(result,user.getPassword(),this.getName());
            return info;
        }
        //返回null，会抛出异常，标识用户名和密码不匹配
        return null;
    }
}
