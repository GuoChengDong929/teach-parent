package com.access.business.log.controller;

import com.access.business.log.service.OperationLogService;
import com.teach.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description:
 * @Author 韩雪松
 * @Date 2019/12/25
 **/
@RestController
@RequestMapping("/operation")
public class OperationLogController {


    @Autowired
    private OperationLogService operationLogService;

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public Result list(@RequestBody Map<String,Object> map){
        return operationLogService.list(map);
    }
}
