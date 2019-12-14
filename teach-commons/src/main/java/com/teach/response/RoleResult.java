package com.teach.response;

import com.teach.entity.access.Permission;
import com.teach.entity.access.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RoleResult implements Serializable {

    private String id;

    private String name; //角色名

    private String description; //说明


    private List<String> permIds = new ArrayList<>();

    public RoleResult(Role role) {
        BeanUtils.copyProperties(role,this);
        for (Permission perm : role.getPermissions()) {
            this.permIds.add(perm.getId());
        }
    }
}
