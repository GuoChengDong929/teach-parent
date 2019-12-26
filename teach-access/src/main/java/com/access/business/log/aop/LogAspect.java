package com.access.business.log.aop;

import com.access.business.log.service.ErrorLogService;
import com.access.business.log.service.OperationLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.teach.aop.OperationProperties;
import com.teach.entity.log.SysErrorLog;
import com.teach.entity.log.SysOperationLog;
import com.teach.response.ProfileResult;
import com.teach.utils.HttpContextUtil;
import com.teach.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Description:
 * @Author 韩雪松
 * @Date 2019/12/25
 **/
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private OperationProperties operationProperties;

    @Autowired
    private OperationLogService operationLogService;


    @Autowired
    private ErrorLogService errorLogService;

    @Pointcut("@annotation(com.teach.aop.Log)")
    public void pointcut(){}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        result = point.proceed();

        //获取request
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        //设置ip地址
        String ip = IPUtil.getIpAddr(request);

        //String ip = "127.0.0.1";



        String username = "";

        if(operationProperties.isOpenAopLog()){ //保存日志
            ProfileResult profileResult = (ProfileResult) SecurityUtils.getSubject().getPrincipal();
            String nickName = profileResult.getNickName();
            username = nickName;

            //装载系统日志类
            SysOperationLog log = new SysOperationLog();
            log.setUsername(username);
            log.setIp(ip);
            log.setTime(beginTime);
            operationLogService.save(point,log);
        }

        return result;
    }

    @AfterThrowing(pointcut = "pointcut()",throwing = "e")
    public void handleThrowing(JoinPoint joinPoint,Exception e) throws JsonProcessingException {
        String className = joinPoint.getTarget().getClass().getName();//获得类名
        String methodName = joinPoint.getSignature().getName();//获得方法名称
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ipAddr = IPUtil.getIpAddr(request);//获得远程ip

        SysErrorLog sysErrorLog = new SysErrorLog();
        sysErrorLog.setIp(ipAddr);//远程访问主机ip
        sysErrorLog.setCreateTime(new Date());//时间
        sysErrorLog.setMessage(e.getMessage());//异常信息
        sysErrorLog.setMethod(methodName+"()");//异常方法
        sysErrorLog.setErrorType(e.getClass().getSimpleName()+".class");//异常类型
        errorLogService.saveErrorLog(joinPoint,sysErrorLog);
    }

}
