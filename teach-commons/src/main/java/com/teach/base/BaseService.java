package com.teach.base;

import com.teach.entity.access.User;
import com.teach.response.ProfileResult;
import org.apache.shiro.SecurityUtils;

public class BaseService {


    public static User currentUser(){

        ProfileResult profileResult = (ProfileResult) SecurityUtils.getSubject().getPrincipal();
        String id = profileResult.getId();
        String nickName = profileResult.getNickName();
        String username = profileResult.getUsername();

        User user = new User();
        user.setId(id);
        user.setNickName(nickName);
        user.setUsername(username);

        return user;
    }


}
