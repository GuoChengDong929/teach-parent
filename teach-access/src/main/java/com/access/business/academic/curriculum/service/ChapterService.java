package com.access.business.academic.curriculum.service;

import com.access.business.academic.curriculum.mapper.ChapterMapper;
import com.access.business.academic.curriculum.mapper.CurriculumMapper;
import com.access.business.academic.question.mapper.AskMapper;
import com.access.business.academic.question.mapper.SelectionMapper;
import com.access.business.academic.question.mapper.SingleMapper;
import com.access.business.academic.question.mapper.UpperMapper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teach.base.BaseService;
import com.teach.entity.academic.curriculum.Chapter;
import com.teach.entity.academic.curriculum.Curriculum;
import com.teach.entity.academic.question.Ask;
import com.teach.entity.academic.question.Selection;
import com.teach.entity.academic.question.Single;
import com.teach.entity.academic.question.Upper;
import com.teach.error.CommonException;
import com.teach.response.PageResult;
import com.teach.response.ProfileResult;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ChapterService extends BaseService {

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private SingleMapper singleMapper;

    @Autowired
    private SelectionMapper selectionMapper;

    @Autowired
    private AskMapper askMapper;

    @Autowired
    private UpperMapper upperMapper;


    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


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
        chapter.setModifyId(currentUser().getId());
        chapter.setModifyUser(currentUser().getNickName());
        chapter.setModifyTime(new Date());
        chapterMapper.insert(chapter);
        return Result.SUCCESS();
    }

    public Result update(Chapter chapter) {
        Chapter target = chapterMapper.selectById(chapter.getId());
        BeanUtils.copyProperties(chapter,target);
        target.setModifyId(currentUser().getId());
        target.setModifyUser(currentUser().getNickName());
        target.setModifyTime(new Date());
        chapterMapper.updateById(target);
        return Result.SUCCESS();
    }

    public Result findChapters() {
        return new Result(ResultCode.SUCCESS,chapterMapper.selectList(null));
    }

    public Result getChapterList(String curriculumId) throws CommonException {


        ProfileResult profileResult = (ProfileResult) SecurityUtils.getSubject().getPrincipal();

        String studentId = profileResult.getId();

        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("curriculum_id",curriculumId);

        queryWrapper.orderByDesc("sort_number");

        List<Chapter> chapters = chapterMapper.selectList(queryWrapper);

        if(chapters != null && chapters.size() > 0){
            for (Chapter chapter : chapters) {
                QueryWrapper<Single> singleQueryWrapper = new QueryWrapper<>();
                singleQueryWrapper.eq("chapter_id",chapter.getId());
                //List<Single> singles = singleMapper.selectList(singleQueryWrapper);
                Integer singleCount = singleMapper.selectCount(singleQueryWrapper);
                //chapter.setSingleList(singles);

                QueryWrapper<Selection> selectionQueryWrapper = new QueryWrapper<>();
                selectionQueryWrapper.eq("chapter_id",chapter.getId());
                //List<Selection> selections = selectionMapper.selectList(selectionQueryWrapper);
                //chapter.setSelectionList(selections);
                Integer selectionCount = selectionMapper.selectCount(selectionQueryWrapper);

                QueryWrapper<Ask> askQueryWrapper = new QueryWrapper<>();
                askQueryWrapper.eq("chapter_id",chapter.getId());
                //List<Ask> asks = askMapper.selectList(askQueryWrapper);
                //chapter.setAskList(asks);
                Integer askCount = askMapper.selectCount(askQueryWrapper);

                QueryWrapper<Upper> upperQueryWrapper = new QueryWrapper<>();
                upperQueryWrapper.eq("chapter_id",chapter.getId());
                //List<Upper> uppers = upperMapper.selectList(upperQueryWrapper);
                //chapter.setUpperList(uppers);
                Integer upperCount = upperMapper.selectCount(upperQueryWrapper);

                chapter.setTotal(singleCount + selectionCount + askCount + upperCount);

                Object o = redisTemplate.opsForValue().get("student#prush#1#"+studentId); // 1# 代表学生没有把试题<不再显示>
                List<String> objects = JSONObject.parseArray((String) o,String.class);
                if(objects == null){
                    chapter.setOkCount(0);
                }else{
                    chapter.setOkCount(objects.size());
                }

                Object obj = redisTemplate.opsForValue().get("student#prush#0#"+studentId); // 0# 代表学生没有把试题<不再显示>
                List<String> object = JSONObject.parseArray((String) obj,String.class);

                if(obj == null){
                    chapter.setNoCount(0);
                }else{
                    chapter.setNoCount(object.size());
                }
            }
        }

        return new Result(ResultCode.SUCCESS,chapters);
    }
}
