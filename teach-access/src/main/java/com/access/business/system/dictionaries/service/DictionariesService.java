package com.access.business.system.dictionaries.service;

import com.access.business.system.dictionaries.mapper.DictionariesMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teach.base.BaseService;
import com.teach.entity.system.dictionaries.Dictionaries;
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
@SuppressWarnings("all")
public class DictionariesService extends BaseService {

    @Autowired
    private DictionariesMapper dictionariesMapper;

    public Result list(Map<String, Object> map) {

        Integer page = Integer.parseInt(map.get("page").toString());
        Integer size = Integer.parseInt(map.get("size").toString());

        QueryWrapper<Dictionaries> queryWrapper = new QueryWrapper<>();
        if(map.get("name") != null) queryWrapper.like("name",map.get("name").toString());
        if(map.get("code") != null) queryWrapper.eq("code",map.get("code").toString());
        if(map.get("useScenarios") != null) queryWrapper.like("use_scenarios",map.get("useScenarios").toString());
        queryWrapper.orderByDesc("create_time");

        IPage<Dictionaries> iPage = new Page<>(page,size);

        IPage<Dictionaries> result = dictionariesMapper.selectPage(iPage, queryWrapper);
        PageResult<Dictionaries> pageResult = new PageResult<>(result.getTotal(),result.getRecords());
        return new Result(ResultCode.SUCCESS,pageResult);

    }

    public Result save(Dictionaries dictionaries) {
        dictionaries.setCreateTime(new Date());
        dictionaries.setModifyId(currentUser().getId());
        dictionaries.setModifyUser(currentUser().getNickName());
        dictionaries.setModifyTime(new Date());
        dictionariesMapper.insert(dictionaries);
        return Result.SUCCESS();
    }

    public Result update(Dictionaries dictionaries) {
        Dictionaries target = dictionariesMapper.selectById(dictionaries.getId());
        BeanUtils.copyProperties(dictionaries,target);
        target.setModifyId(currentUser().getId());
        target.setModifyUser(currentUser().getNickName());
        target.setModifyTime(new Date());
        dictionariesMapper.updateById(target);
        return Result.SUCCESS();
    }

    public Result getDictionariesByCode(Map<String, Object> map) {
        QueryWrapper<Dictionaries> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code",map.get("code").toString());
        queryWrapper.orderByDesc("create_time");
        List<Dictionaries> dictionaries = dictionariesMapper.selectList(queryWrapper);
        return new Result(ResultCode.SUCCESS,dictionaries);
    }
}
