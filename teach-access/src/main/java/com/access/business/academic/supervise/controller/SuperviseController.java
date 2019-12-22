package com.access.business.academic.supervise.controller;

import com.access.business.academic.supervise.service.SuperviseService;
import com.teach.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Map;

/**
 * @Description:
 * @Author 韩雪松
 * @Date 2019/12/19
 **/
@RestController
@RequestMapping("/supervise")
public class SuperviseController {

    @Autowired
    private SuperviseService superviseService;

    @RequestMapping(value = "/getScoresByDate",method = RequestMethod.POST)
    public Result getScoresByDate(@RequestBody Map<String,Object> map) throws ParseException {
        return superviseService.getScoresByDate(map);
    }
}
