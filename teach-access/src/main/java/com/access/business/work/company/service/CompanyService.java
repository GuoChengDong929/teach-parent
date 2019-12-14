package com.access.business.work.company.service;

import com.access.business.work.company.mapper.CompanyMapper;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    public Result getCompanies() {
        return new Result(ResultCode.SUCCESS,companyMapper.selectList(null));
    }
}
