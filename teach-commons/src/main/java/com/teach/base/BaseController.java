package com.teach.base;

import com.teach.response.ProfileResult;
import lombok.Data;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Data
public class BaseController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String nickName;


    @ModelAttribute
    public void setResAnReq(HttpServletRequest request,HttpServletResponse response) {
        this.request = request;
        this.response = response;

        //获取session中的安全数据
        Subject subject = SecurityUtils.getSubject();
        //1.subject获取所有的安全数据集合
        PrincipalCollection principals = subject.getPrincipals();
        if(principals != null && !principals.isEmpty()){
            //2.获取安全数据
            ProfileResult result = (ProfileResult)principals.getPrimaryPrincipal();
            this.nickName = result.getNickName();
        }
    }

}
