package com.access.business.academic.curriculum.service;

import com.access.business.academic.curriculum.mapper.ChapterMapper;
import com.access.business.academic.curriculum.mapper.CurriculumMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teach.base.BaseService;
import com.teach.entity.academic.curriculum.Chapter;
import com.teach.entity.academic.curriculum.Curriculum;
import com.teach.response.PageResult;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CurriculumService extends BaseService {


    @Autowired
    private CurriculumMapper curriculumMapper;


    public Result save(Curriculum curriculum) {
        curriculum.setCreateTime(new Date());
        curriculum.setModifyId(currentUser().getId());
        curriculum.setModifyUser(currentUser().getNickName());
        curriculum.setModifyTime(new Date());
        curriculumMapper.insert(curriculum);
        return Result.SUCCESS();
    }

    public Result update(Curriculum curriculum) {
        Curriculum target = curriculumMapper.selectById(curriculum.getId());
        BeanUtils.copyProperties(curriculum,target);
        target.setModifyId(currentUser().getId());
        target.setModifyUser(currentUser().getNickName());
        target.setModifyTime(new Date());
        curriculumMapper.updateById(target);
        return Result.SUCCESS();
    }

    public Result list(Map<String,Object> map){
        IPage<Curriculum> iPage = new Page<>(Integer.parseInt(map.get("page").toString()),Integer.parseInt(map.get("size").toString()));

        IPage<Curriculum> result = curriculumMapper.selectPage(iPage, null);

        PageResult<Curriculum> pageResult = new PageResult<>();

        pageResult.setTotal(result.getTotal());
        pageResult.setRows(result.getRecords());

        return new Result(ResultCode.SUCCESS,pageResult);
    }

    public Result findAll() {
        QueryWrapper<Curriculum> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sort_number");
        return new Result(ResultCode.SUCCESS,curriculumMapper.selectList(queryWrapper));
    }
}
