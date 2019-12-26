package com.teach.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teach.entity.access.User;
import com.teach.entity.system.dictionaries.Dictionaries;
import com.teach.response.PageResult;
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
