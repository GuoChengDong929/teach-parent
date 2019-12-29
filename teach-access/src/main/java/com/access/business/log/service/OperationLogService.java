package com.access.business.log.service;

import cn.hutool.log.level.ErrorLog;
import com.access.business.log.mapper.OperationLogMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teach.aop.Log;
import com.teach.entity.log.SysErrorLog;
import com.teach.entity.log.SysOperationLog;
import com.teach.response.PageResult;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import com.teach.utils.AddressUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Description:
 * @Author 韩雪松
 * @Date 2019/12/25
 **/
@Service
@Transactional
public class OperationLogService {

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private OperationLogMapper operationLogMapper;



    public Result list(Map<String, Object> map) {

        Object username = map.get("username");

        QueryWrapper<SysOperationLog> queryWrapper = new QueryWrapper<>();

        if(username != null){
            queryWrapper.like("username",username.toString());
        }

        Object operation = map.get("operation");

        if(operation != null){
            queryWrapper.like("operation",operation.toString());
        }

        Integer page = Integer.parseInt(map.get("page").toString());
        Integer size = Integer.parseInt(map.get("size").toString());

        IPage<SysOperationLog> iPage = new Page<>(page,size);


        queryWrapper.orderByDesc("create_time");


        IPage<SysOperationLog> result = operationLogMapper.selectPage(iPage, queryWrapper);


        PageResult<SysOperationLog> pageResult = new PageResult<>(result.getTotal(),result.getRecords());

        return new Result(ResultCode.SUCCESS,pageResult);
    }


    public void save(ProceedingJoinPoint point, SysOperationLog log) throws JsonProcessingException {

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);

        if(logAnnotation != null){
            log.setOperation(logAnnotation.value());
        }

        String methodName = signature.getName(); //请求的方法名

        log.setMethod(methodName + "()");

        Object[] args = point.getArgs(); //请求的方法参数值

        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer(); //请求的方法参数名称

        String[] parameterNames = u.getParameterNames(method);

        if(args != null &&parameterNames != null){
            StringBuilder params = new StringBuilder();
            params = this.handleParams(params,args, Arrays.asList(parameterNames));
            log.setParams(params.toString());
        }

        log.setCreateTime(new Date());


        Long beginTime = log.getTime();

        Long time = System.currentTimeMillis() - beginTime;

        log.setTime(time);

        operationLogMapper.insert(log);

    }



    /**
     * 获得参数
     * @param params
     * @param args
     * @param paramNames
     * @return
     * @throws JsonProcessingException
     */
    private StringBuilder handleParams(StringBuilder params, Object[] args, List paramNames) throws JsonProcessingException {
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

}
