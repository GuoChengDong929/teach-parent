package com.access.business.academic.curriculum.service;

import com.access.business.academic.curriculum.mapper.ChapterMapper;
import com.access.business.academic.curriculum.mapper.CurriculumMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.teach.entity.academic.curriculum.Chapter;
import com.teach.entity.academic.curriculum.Curriculum;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CurriculumService {


    @Autowired
    private CurriculumMapper curriculumMapper;


    public Result save(Curriculum curriculum) {
        curriculum.setCreateTime(new Date());
        curriculumMapper.insert(curriculum);
        return Result.SUCCESS();
    }

    public Result update(Curriculum curriculum) {
        Curriculum target = curriculumMapper.selectById(curriculum.getId());
        BeanUtils.copyProperties(curriculum,target);
        curriculumMapper.updateById(target);
        return Result.SUCCESS();
    }


    public Result findAll() {

        QueryWrapper<Curriculum> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sort_number");
        return new Result(ResultCode.SUCCESS,curriculumMapper.selectList(queryWrapper));
    }
}
