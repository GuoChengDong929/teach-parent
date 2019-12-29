package com.access.business.work.company.controller;

import com.access.business.work.company.service.CompanyService;
import com.teach.aop.Log;
import com.teach.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
public class CompanyController {


    @Autowired
    private CompanyService companyService;

    @Log("获取所有企业")
    @RequestMapping(value = "/getCompanies",method = RequestMethod.GET)
    public Result getCompanies(){
        return companyService.getCompanies();
    }
}
