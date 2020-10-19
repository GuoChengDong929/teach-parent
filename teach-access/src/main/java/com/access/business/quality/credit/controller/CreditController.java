package com.access.business.quality.credit.controller;

import com.access.business.quality.credit.service.CreditService;
import com.access.business.quality.transact.service.ClassesService;
import com.teach.entity.quality.credit.Credits;
import com.teach.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/credit")
public class CreditController {

    @Autowired
    private CreditService creditService;

    @Autowired
    private ClassesService classesService;

    @RequestMapping(value = "getClassesName", method = RequestMethod.GET)
    public Result getClassesName() {
        return classesService.getClassesName();
    }

    @RequestMapping(value = "getCreditList", method = RequestMethod.POST)
    public Result getCreditList(@RequestBody Map<String, Object> map) {
        return creditService.getCreditList(map);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody Credits credit) {
        return creditService.save(credit);
    }

}
