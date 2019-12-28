package com.access.business.student.service;

import com.access.business.academic.question.mapper.AskMapper;
import com.access.business.academic.question.mapper.SelectionMapper;
import com.access.business.academic.question.mapper.SingleMapper;
import com.access.business.academic.question.mapper.UpperMapper;
import com.access.business.academic.supervise.mapper.StudentAskMapper;
import com.access.business.academic.supervise.mapper.StudentSelectionMapper;
import com.access.business.academic.supervise.mapper.StudentSingleMapper;
import com.access.business.academic.supervise.mapper.StudentUpperMapper;
import com.access.business.access.repository.UserRepository;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teach.base.BaseService;
import com.teach.entity.academic.question.Ask;
import com.teach.entity.academic.question.Selection;
import com.teach.entity.academic.question.Single;
import com.teach.entity.academic.question.Upper;
import com.teach.entity.access.User;
import com.teach.entity.student.brush.StudentAsk;
import com.teach.entity.student.brush.StudentSelection;
import com.teach.entity.student.brush.StudentSingle;
import com.teach.entity.student.brush.StudentUpper;
import com.teach.entity.vo.QuestionTypeVo;
import com.teach.error.CommonException;
import com.teach.response.PageResult;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description:
 * @Author 韩雪松
 * @Date 2019/12/26
 **/
@Service
@Transactional
public class StudentBrushService extends BaseService {


    @Autowired
    private SingleMapper singleMapper;

    @Autowired
    private SelectionMapper selectionMapper;

    @Autowired
    private AskMapper askMapper;

    @Autowired
    private UpperMapper upperMapper;

    @Autowired
    private StudentSingleMapper studentSingleMapper;

    @Autowired
    private StudentSelectionMapper studentSelectionMapper;

    @Autowired
    private StudentAskMapper studentAskMapper;

    @Autowired
    private StudentUpperMapper studentUpperMapper;

    @Autowired
    private UserRepository userRepository;


    public Result getQuestionsByChapter(Map<String, Object> map) throws CommonException {


        String studentId = currentUser().getId();

        String chapterId = map.get("chapterId").toString();

        String type = map.get("type").toString();//1 单选题 2多选题 3问答题 4上机题 ok已回答 no已屏蔽

        switch (type){
            case "1":
                QueryWrapper<Single> singleQueryWrapper = new QueryWrapper<>();
                singleQueryWrapper.eq("chapter_id",chapterId);
                List<Single> singles = singleMapper.selectList(singleQueryWrapper);

                //查找st_student_single表,当前章节下是否有已答过的题, 如果有则出新试题的时候刨除
                QueryWrapper<StudentSingle> studentSingleQueryWrapper = new QueryWrapper<>();
                studentSingleQueryWrapper.eq("chapter_id",chapterId);
                studentSingleQueryWrapper.eq("student_id",studentId);
                List<StudentSingle> studentSingles = studentSingleMapper.selectList(studentSingleQueryWrapper);

                if(studentSingles != null && studentSingles.size() > 0){
                    for (StudentSingle studentSingle : studentSingles) {
                        String singleId = studentSingle.getSingleId(); //已答过的单选题id
                        if(singles != null && singles.size() > 0){
                            Iterator<Single> iterator = singles.iterator();
                            while(iterator.hasNext()){
                                Single single = iterator.next();
                                if(single.getId().equals(singleId)){
                                    iterator.remove();
                                }
                            }
                        }
                    }
                }



                if(singles != null && singles.size() > 0){
                    Collections.shuffle(singles,new Random(3)); //打乱3次
                    List<Single> singleList = singles.subList(0, singles.size() >= 20 ? 20 : singles.size());
                    return new Result(ResultCode.SUCCESS,singleList);
                }else{
                    throw new CommonException(ResultCode.SINGLE_IS_ZERO);
                }

            case "2":
                QueryWrapper<Selection> selectionQueryWrapper = new QueryWrapper<>();
                selectionQueryWrapper.eq("chapter_id",chapterId);
                List<Selection> selections = selectionMapper.selectList(selectionQueryWrapper);

                //查找st_student_selection表,当前章节下是否有已答过的题, 如果有则出新试题的时候刨除
                QueryWrapper<StudentSelection> studentSelectionQueryWrapper = new QueryWrapper<>();
                studentSelectionQueryWrapper.eq("chapter_id",chapterId);
                studentSelectionQueryWrapper.eq("student_id",studentId);
                List<StudentSelection> studentSelections = studentSelectionMapper.selectList(studentSelectionQueryWrapper);

                if(studentSelections != null && studentSelections.size() > 0){
                    for (StudentSelection studentSelection : studentSelections) {
                        String selectionId = studentSelection.getSelectionId(); //已答过的多选题id
                        if(selections != null && selections.size() > 0){
                            Iterator<Selection> iterator = selections.iterator();
                            while(iterator.hasNext()){
                                Selection selection = iterator.next();
                                if(selection.getId().equals(selectionId)){
                                    iterator.remove();
                                }
                            }
                        }
                    }
                }


                if(selections != null && selections.size() > 0){
                    Collections.shuffle(selections,new Random(3)); //打乱3次
                    List<Selection> selectionList = selections.subList(0, selections.size() >= 20 ? 20 : selections.size());
                    return new Result(ResultCode.SUCCESS,selectionList);
                }else{
                    throw new CommonException(ResultCode.SELECTION_IS_ZERO);
                }

            case "3":
                QueryWrapper<Ask> askQueryWrapper = new QueryWrapper<>();
                askQueryWrapper.eq("chapter_id",chapterId);
                List<Ask> asks = askMapper.selectList(askQueryWrapper);


                QueryWrapper<StudentAsk> studentAskQueryWrapper = new QueryWrapper<>();
                studentAskQueryWrapper.eq("chapter_id",chapterId);
                studentAskQueryWrapper.eq("student_id",studentId);
                List<StudentAsk> studentAsks = studentAskMapper.selectList(studentAskQueryWrapper);

                if(studentAsks != null && studentAsks.size() > 0){
                    for (StudentAsk studentAsk : studentAsks) {
                        String askId = studentAsk.getAskId(); //已答过的问答题id
                        if(asks != null && asks.size() > 0){
                            Iterator<Ask> iterator = asks.iterator();
                            while(iterator.hasNext()){
                                Ask ask = iterator.next();
                                if(ask.getId().equals(askId)){
                                    iterator.remove();
                                }
                            }
                        }
                    }
                }


                if(asks != null && asks.size() > 0){
                    Collections.shuffle(asks,new Random(3)); //打乱3次
                    List<Ask> askList = asks.subList(0, asks.size() >= 5 ? 5 : asks.size());
                    return new Result(ResultCode.SUCCESS,askList);
                }else{
                    throw new CommonException(ResultCode.ASK_IS_ZERO);
                }

            case "4":
                QueryWrapper<Upper> upperQueryWrapper = new QueryWrapper<>();
                upperQueryWrapper.eq("chapter_id",chapterId);

                List<Upper> uppers = upperMapper.selectList(upperQueryWrapper);


                QueryWrapper<StudentUpper> studentUpperQueryWrapper = new QueryWrapper<>();
                studentUpperQueryWrapper.eq("chapter_id",chapterId);
                studentUpperQueryWrapper.eq("student_id",studentId);
                List<StudentUpper> studentUppers = studentUpperMapper.selectList(studentUpperQueryWrapper);

                if(studentUppers != null && studentUppers.size() > 0){
                    for (StudentUpper studentUpper : studentUppers) {
                        String upperId = studentUpper.getUpperId(); //已答过的问答题id
                        if(uppers != null && uppers.size() > 0){
                            Iterator<Upper> iterator = uppers.iterator();
                            while(iterator.hasNext()){
                                Upper upper = iterator.next();
                                if(upper.getId().equals(upperId)){
                                    iterator.remove();
                                }
                            }
                        }
                    }
                }



                if(uppers != null && uppers.size() > 0){

                    return new Result(ResultCode.SUCCESS,uppers );
                }else{
                    throw new CommonException(ResultCode.UPPER_IS_ZERO);
                }
            case "ok":

                QuestionTypeVo vo = new QuestionTypeVo();

                List<Single> singlesList = new ArrayList<>();
                List<Selection> selectionsList = new ArrayList<>();
                List<Ask> asksList = new ArrayList<>();
                List<Upper> uppersList = new ArrayList<>();

                QueryWrapper<StudentSingle> studentSingleWrapper = new QueryWrapper<>();
                studentSingleWrapper.eq("chapter_id",chapterId);
                studentSingleWrapper.eq("student_id",studentId);
                List<StudentSingle> studentSinglesList = studentSingleMapper.selectList(studentSingleWrapper);

                if(studentSinglesList != null && studentSinglesList.size() > 0){
                    for (StudentSingle studentSingle : studentSinglesList) {
                        String singleId = studentSingle.getSingleId();
                        Single single = singleMapper.selectById(singleId);
                        singlesList.add(single);
                    }
                }


                QueryWrapper<StudentSelection> studentSelectionWrapper = new QueryWrapper<>();
                studentSelectionWrapper.eq("chapter_id",chapterId);
                studentSelectionWrapper.eq("student_id",studentId);
                List<StudentSelection> studentSelectionsList = studentSelectionMapper.selectList(studentSelectionWrapper);
                if(studentSelectionsList != null && studentSelectionsList.size() > 0){
                    for (StudentSelection studentSelection : studentSelectionsList) {
                        String selectionId = studentSelection.getSelectionId();
                        Selection selection = selectionMapper.selectById(selectionId);
                        selectionsList.add(selection);
                    }
                }


                QueryWrapper<StudentAsk> studentAskWrapper = new QueryWrapper<>();
                studentAskWrapper.eq("chapter_id",chapterId);
                studentAskWrapper.eq("student_id",studentId);
                List<StudentAsk> studentAsksList = studentAskMapper.selectList(studentAskWrapper);
                if(studentAsksList != null && studentAsksList.size() > 0){
                    for (StudentAsk studentAsk : studentAsksList) {
                        String askId = studentAsk.getAskId();
                        Ask ask = askMapper.selectById(askId);
                        asksList.add(ask);
                    }
                }

                QueryWrapper<StudentUpper> studentUpperWrapper = new QueryWrapper<>();
                studentUpperWrapper.eq("chapter_id",chapterId);
                studentUpperWrapper.eq("student_id",studentId);
                List<StudentUpper> studentUppersList = studentUpperMapper.selectList(studentUpperWrapper);
                if(studentUppersList != null && studentUppersList.size() > 0){
                    for (StudentUpper studentUpper : studentUppersList) {
                        String upperId = studentUpper.getUpperId();
                        Upper upper = upperMapper.selectById(upperId);
                        uppersList.add(upper);
                    }
                }

                if(singlesList.size() == 0 && selectionsList.size() == 0 && asksList.size() == 0 && uppersList.size() == 0){
                    throw new CommonException(ResultCode.OK_IS_ZERO);
                }
                vo.setSingleList(singlesList);
                vo.setSelectionList(selectionsList);
                vo.setAskList(asksList);
                vo.setUpperList(uppersList);

                return new Result(ResultCode.SUCCESS,vo);

            default:
                throw new CommonException(ResultCode.FAIL);
        }


    }

    public Result updateOk(Map<String, Object> map) {

        String currentId = currentUser().getId();

        User user = userRepository.findById(currentId).get();

        String userType = user.getType();

        String level = user.getLevel();

        if("1".equals(userType) && "user".equals(level)){
            return Result.ERROR();
        }


        String type = map.get("type").toString();

        String chapterId = map.get("chapterId").toString();

        String studentId = currentUser().getId();

        if("1".equals(type)){
            String singleId = map.get("singleId").toString();
            StudentSingle studentSingle = new StudentSingle();
            studentSingle.setChapterId(chapterId);
            studentSingle.setStudentId(studentId);
            studentSingle.setSingleId(singleId);
            studentSingleMapper.insert(studentSingle);
        }else if("2".equals(type)){
            String selectionId = map.get("selectionId").toString();
            StudentSelection studentSelection = new StudentSelection();
            studentSelection.setChapterId(chapterId);
            studentSelection.setStudentId(studentId);
            studentSelection.setSelectionId(selectionId);
            studentSelectionMapper.insert(studentSelection);
        }else if("3".equals(type)){
            String askId = map.get("askId").toString();
            StudentAsk studentAsk = new StudentAsk();
            studentAsk.setAskId(askId);
            studentAsk.setChapterId(chapterId);
            studentAsk.setStudentId(studentId);
            studentAskMapper.insert(studentAsk);
        }else if("4".equals(type)){
            String upperId = map.get("upperId").toString();
            StudentUpper studentUpper = new StudentUpper();
            studentUpper.setChapterId(chapterId);
            studentUpper.setStudentId(studentId);
            studentUpper.setUpperId(upperId);
            studentUpperMapper.insert(studentUpper);
        }

        return Result.SUCCESS();
    }

    public Result removeOk(Map<String, Object> map) {
        List<String> idsArray = (List<String>) map.get("idsArray");
        String type = map.get("type").toString();
        String chapterId = map.get("chapterId").toString();
        String studentId = currentUser().getId();

        if("1".equals(type)){
            for (String singleId : idsArray) {
                QueryWrapper<StudentSingle> studentSingleQueryWrapper = new QueryWrapper<>();
                studentSingleQueryWrapper.eq("student_id",studentId);
                studentSingleQueryWrapper.eq("chapter_id",chapterId);
                studentSingleQueryWrapper.eq("single_id",singleId);
                studentSingleMapper.delete(studentSingleQueryWrapper);
            }
        }else if("2".equals(type)){
            for (String selectionId : idsArray) {
                QueryWrapper<StudentSelection> studentSelectionQueryWrapper = new QueryWrapper<>();
                studentSelectionQueryWrapper.eq("student_id",studentId);
                studentSelectionQueryWrapper.eq("chapter_id",chapterId);
                studentSelectionQueryWrapper.eq("selection_id",selectionId);
                studentSelectionMapper.delete(studentSelectionQueryWrapper);
            }
        }else if("3".equals(type)){
            for (String askId : idsArray) {
                QueryWrapper<StudentAsk> studentAskQueryWrapper = new QueryWrapper<>();
                studentAskQueryWrapper.eq("student_id",studentId);
                studentAskQueryWrapper.eq("chapter_id",chapterId);
                studentAskQueryWrapper.eq("ask_id",askId);
                studentAskMapper.delete(studentAskQueryWrapper);
            }
        }else if("4".equals(type)){
            for (String upperId : idsArray) {
                QueryWrapper<StudentUpper> studentUpperQueryWrapper = new QueryWrapper<>();
                studentUpperQueryWrapper.eq("student_id",studentId);
                studentUpperQueryWrapper.eq("chapter_id",chapterId);
                studentUpperQueryWrapper.eq("upper_id",upperId);
                studentUpperMapper.delete(studentUpperQueryWrapper);
            }
        }

        return Result.SUCCESS();
    }
}
