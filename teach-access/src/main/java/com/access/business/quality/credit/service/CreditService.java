package com.access.business.quality.credit.service;

import com.access.business.access.repository.UserRepository;
import com.access.business.quality.credit.mapper.CreditMapper;
import com.access.business.quality.student.mapper.StudentMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.teach.base.BaseService;
import com.teach.entity.access.User;
import com.teach.entity.quality.credit.Credits;
import com.teach.entity.quality.student.Student;
import com.teach.entity.vo.UserStudentVo;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import com.teach.utils.IdWorker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class CreditService extends BaseService {

    @Autowired
    private CreditMapper creditMapper;

    @Autowired
    private IdWorker idWorker;


    public Result getCreditList(Map<String, Object> map) {
        String id = map.get("id").toString();

        QueryWrapper<Credits> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id", id);

        List<Credits> credits = creditMapper.selectList(queryWrapper);

        return new Result(ResultCode.SUCCESS, credits);
    }

    public Result save(Credits credit) {
        credit.setId(idWorker.nextId() + "");
        credit.setCreateTime(new Date());
        creditMapper.insert(credit);
        return Result.SUCCESS();
    }
}
