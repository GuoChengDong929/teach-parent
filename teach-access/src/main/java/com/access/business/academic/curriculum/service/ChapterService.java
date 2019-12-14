package com.access.business.academic.curriculum.service;

import com.access.business.academic.curriculum.mapper.ChapterMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teach.entity.academic.curriculum.Chapter;
import com.teach.response.PageResult;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
@Transactional
public class ChapterService {

    @Autowired
    private ChapterMapper chapterMapper;

    public Result list(Map<String, Object> map) {
        Integer page = Integer.parseInt(map.get("page").toString());
        Integer size = Integer.parseInt(map.get("size").toString());

        String curriculumId = map.get("curriculumId").toString();

        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("curriculum_id",curriculumId);
        queryWrapper.orderByDesc("sort_number");

        IPage<Chapter> iPage = new Page<>(page,size);

        IPage<Chapter> result = chapterMapper.selectPage(iPage, queryWrapper);
        PageResult<Chapter> pageResult = new PageResult<>(result.getTotal(),result.getRecords());

        return new Result(ResultCode.SUCCESS,pageResult);
    }

    public Result save(Chapter chapter) {
        chapter.setCreateTime(new Date());
        chapterMapper.insert(chapter);
        return Result.SUCCESS();
    }

    public Result update(Chapter chapter) {
        Chapter target = chapterMapper.selectById(chapter.getId());
        BeanUtils.copyProperties(chapter,target);
        chapterMapper.updateById(target);
        return Result.SUCCESS();
    }

    public Result findChapters() {
        return new Result(ResultCode.SUCCESS,chapterMapper.selectList(null));
    }
}
