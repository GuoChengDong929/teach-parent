package com.access.business.log.service;


import com.access.business.log.mapper.ErrorLogMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teach.aop.Log;
import com.teach.base.BaseService;
import com.teach.entity.log.SysErrorLog;
import com.teach.response.PageResult;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;


@Service
@Transactional
public class ErrorLogService extends BaseService {


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ErrorLogMapper errorLogMapper;


    public void saveErrorLog(JoinPoint joinPoint, SysErrorLog sysErrorLog) throws JsonProcessingException {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        //获得参数列表
        Object[] args = joinPoint.getArgs();
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            StringBuilder params = new StringBuilder();
            params = handleParams(params, args, Arrays.asList(paramNames));
            sysErrorLog.setParams(params.toString());
        }

        //获得注解信息
        Log annotation = method.getAnnotation(Log.class);
        String value = annotation.value();
        sysErrorLog.setOperation(value);

        sysErrorLog.setModifyId(currentUser().getId());
        sysErrorLog.setModifyUser(currentUser().getNickName());


        errorLogMapper.insert(sysErrorLog);
    }



    protected StringBuilder handleParams(StringBuilder params, Object[] args, List paramNames) throws JsonProcessingException {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Map) {
                Set set = ((Map) args[i]).keySet();
                List<Object> list = new ArrayList<>();
                List<Object> paramList = new ArrayList<>();
                for (Object key : set) {
                    list.add(((Map) args[i]).get(key));
                    paramList.add(key);
                }
                return handleParams(params, list.toArray(), paramList);
            } else {
                if (args[i] instanceof Serializable) {
                    Class<?> aClass = args[i].getClass();
                    try {
                        aClass.getDeclaredMethod("toString", new Class[]{null});
                        // 如果不抛出 NoSuchMethodException 异常则存在 toString 方法 ，安全的 writeValueAsString ，否则 走 Object的 toString方法
                        params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i]));
                    } catch (NoSuchMethodException e) {
                        params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i].toString()));
                    }
                } else if (args[i] instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) args[i];
                    params.append(" ").append(paramNames.get(i)).append(": ").append(file.getName());
                } else {
                    params.append(" ").append(paramNames.get(i)).append(": ").append(args[i]);
                }
            }
        }
        return params;
    }

    public Result list(Map<String, Object> map) {





        Integer page = Integer.parseInt(map.get("page").toString());
        Integer size = Integer.parseInt(map.get("size").toString());

        IPage<SysErrorLog> iPage = new Page<>(page,size);

        Object modifyUser = map.get("modifyUser");
        Object operation = map.get("operation");



        QueryWrapper<SysErrorLog> queryWrapper = new QueryWrapper<>();

        if(modifyUser != null) queryWrapper.like("modify_user",modifyUser.toString());

        if(operation != null) queryWrapper.like("operation",operation.toString());

        queryWrapper.orderByDesc("create_time");

        IPage<SysErrorLog> result = errorLogMapper.selectPage(iPage, queryWrapper);

        PageResult<SysErrorLog> pageResult = new PageResult<>(result.getTotal(),result.getRecords());

        return new Result(ResultCode.SUCCESS,pageResult);
    }
}
