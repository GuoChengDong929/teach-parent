package com.access.business.student.service;

import com.access.business.academic.exam.mapper.AskResultMapper;
import com.access.business.academic.exam.mapper.ExamMapper;
import com.access.business.academic.exam.mapper.ScoreMapper;
import com.access.business.academic.question.mapper.AskMapper;
import com.access.business.academic.question.mapper.SelectionMapper;
import com.access.business.academic.question.mapper.SingleMapper;
import com.access.business.quality.student.mapper.StudentMapper;
import com.access.business.student.mapper.SelectionResultMapper;
import com.access.business.student.mapper.SingleResultMapper;
import com.access.business.student.mapper.StudentExamMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teach.entity.academic.exam.*;
import com.teach.entity.academic.question.Selection;
import com.teach.entity.academic.question.Single;
import com.teach.entity.quality.student.Student;
import com.teach.error.CommonException;
import com.teach.response.PageResult;
import com.teach.response.ProfileResult;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import java.util.Date;
import java.util.Map;

/**
 * @Description:
 * @Author 韩雪松
 * @Date 2019/12/12
 **/

@Service
@Transactional
@SuppressWarnings("all")
public class StudentExamService {
    
    @Autowired
    private StudentExamMapper studentExamMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private SingleMapper singleMapper;

    @Autowired
    private SingleResultMapper singleResultMapper;

    @Autowired
    private SelectionMapper selectionMapper;

    @Autowired
    private SelectionResultMapper selectionResultMapper;

    @Autowired
    private AskMapper askMapper;

    @Autowired
    private AskResultMapper askResultMapper;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    public Result getList(Map<String, Object> map) {


        Integer page = Integer.parseInt(map.get("page").toString());
        Integer size = Integer.parseInt(map.get("size").toString());



        ProfileResult profileResult = (ProfileResult) SecurityUtils.getSubject().getPrincipal();
        String id = profileResult.getId();

        //根据当前用户的id来查询所在班级
        Student student = studentMapper.selectById(id);

        String classesId = student.getClassesId();

        //说明该用户还没有班级
        if(classesId == null) return Result.FAIL();


        QueryWrapper<Exam> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("classes_id",classesId);

        IPage<Exam> iPage = new Page<>(page,size);

        IPage<Exam> result = examMapper.selectPage(iPage, null);

        PageResult<Exam> pageResult = new PageResult<>(result.getTotal(),result.getRecords());

        return new Result(ResultCode.SUCCESS,pageResult);

    }

    public Result save(Map<String, Object> map) {

        String[] singleOptions = map.get("singleOptions").toString().split(",");

        String[] selectionOptions = map.get("selectionOptions").toString().split("@");

        String[] askOptions = map.get("askOptions").toString().split("!&");

        Exam exam = examMapper.selectById(map.get("id").toString());  //获得当前试卷实例

        String questionTypeIds = exam.getQuestionTypeIds();

        ProfileResult profileResult = (ProfileResult) SecurityUtils.getSubject().getPrincipal();

        String id = profileResult.getId();

        QueryWrapper<Score> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("student_id",id);

        queryWrapper.eq("exam_id",exam.getId());

        Score score = scoreMapper.selectOne(queryWrapper); //通过学生id和当前试卷的id来查询当前学生的成绩实例

        if("1".equals(score.getStatus())){  //屏蔽为0的学生
            score.setStatus("2");
        }

        Integer upperScore = 0;

        Integer totalScore = 0;

         if (questionTypeIds.contains("1")){
             String singleSuccIds = "";
             String singleErrIds = "";
             Integer singleSucc = 0;
             Integer singleErr = 0;

             Integer singleScore = 0;
             //处理st_score表
             String singleJoins = exam.getSingleJoins();

             String[] singleIdArray = singleJoins.split(",");

             Integer oneSingleScore = exam.getSingleScore(); //单选题每题分数


             for(int i = 0; i < singleIdArray.length; i ++){
                 Single single = singleMapper.selectById(singleIdArray[i]);
                 String singleAsk = single.getSingleAsk();
                 if(singleAsk.equals(singleOptions[i])){
                     singleSuccIds += "," + singleIdArray[i] ;
                     singleSucc += 1;
                     singleScore += oneSingleScore; //单选总分=  单选题每题分数 + 单选题总分
                     totalScore += oneSingleScore;  //总成绩 = 总成绩 + 单选题每题分数
                 }else{
                     singleErrIds += "," + singleIdArray[i];
                     singleErr += 1;
                 }

                 //处理st_single_result表
                 SingleResult singleResult = SingleResult.builder()
                         .singleId(singleIdArray[i])
                         .optionIds(singleOptions[i])
                         .studentId(id)
                         .examId(exam.getId())
                         .build();

                 singleResultMapper.insert(singleResult);
             }

             score.setSingleErr(singleErr);
             score.setSingleErrIds(singleErrIds);
             score.setSingleSucc(singleSucc);
             score.setSingleSuccIds(singleSuccIds);
             score.setSingleScore(singleScore);
         }

         if(questionTypeIds.contains("2")){
             String selectionSuccIds = "";
             String selectionErrIds = "";
             Integer selectionSucc = 0;
             Integer selectionErr = 0;

             Integer selectionScore = 0;

             String selectionJoins = exam.getSelectionJoins();
             String[] selectionIdArray = selectionJoins.split(",");
             Integer selectScore = exam.getSelectionScore(); //多选题每题分数

             for(int i = 0 ; i < selectionIdArray.length; i++){
                 Selection selection = selectionMapper.selectById(selectionIdArray[i]);
                 String selectionAsk = selection.getSelectionAsk();
                 selectionAsk = this.sort(selectionAsk); //处理答案排序  有可能1,4,2,3 处理成1,2,3,4
                 selectionOptions[i] = this.sort(selectionOptions[i]);
                 if(selectionAsk.equals(selectionOptions[i])){
                     selectionSuccIds += "," + selectionIdArray[i];
                     selectionSucc += 1;
                     selectionScore += selectScore;
                     totalScore += selectScore;
                 }else{
                     selectionErrIds += "," + selectionIdArray[i];
                     selectionErr += 1;
                 }

                 //处理st_selection_result表
                 SelectionResult selectionResult = SelectionResult.builder()
                         .selectionId(selectionIdArray[i])
                         .examId(exam.getId())
                         .optionIds(selectionOptions[i])
                         .studentId(id)
                         .build();

                 selectionResultMapper.insert(selectionResult);

             }

             score.setSelectionErr(selectionErr);
             score.setSelectionErrIds(selectionErrIds);
             score.setSelectionSucc(selectionSucc);
             score.setSelectionSuccIds(selectionSuccIds);
             score.setSelectionScore(selectionScore);
         }

        //因为在开始的时候就已经初始化学生的问答题了, 所以在这里是需要修改的
         if(questionTypeIds.contains("3")){
             Integer askScore = 0;
             String askJoins = exam.getAskJoins();
             String[] askIdArray = askJoins.split(",");
             for(int i = 0; i < askIdArray.length ; i ++){
                 QueryWrapper<AskResult> askResultQueryWrapper = new QueryWrapper<>();
                 askResultQueryWrapper.eq("ask_id",askIdArray[i]);
                 askResultQueryWrapper.eq("exam_id",exam.getId());
                 askResultQueryWrapper.eq("student_id",id);

                 AskResult askResult = askResultMapper.selectOne(askResultQueryWrapper);

                 askResult.setAskAnswer(askOptions[i]);

                 askResultMapper.updateById(askResult);
             }
         }

         score.setScore(totalScore);
         score.setExecuteTime(new Date());
         scoreMapper.updateById(score);

         return Result.SUCCESS();


    }


    protected static String sort(String options){
        if(!"".equals(options)){
            String[] split = options.split(",");
            Integer [] target = new Integer[split.length];
            for (int i = 0; i < split.length; i++) {
                target[i] = Integer.parseInt(split[i]);
            }
            Arrays.sort(target);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < target.length; i++) {
                sb.append(",").append(target[i]);
            }
            String substring = sb.toString().substring(1);
            return substring;
        }
        return "";
    }


    public Result getScoreStatus(String id) throws CommonException {
        ProfileResult profileResult = (ProfileResult) SecurityUtils.getSubject().getPrincipal();

        String studentId = profileResult.getId();

        QueryWrapper<Score> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id",studentId);
        queryWrapper.eq("exam_id",id);

        Score score = scoreMapper.selectOne(queryWrapper);

        if(score != null && score.getStatus() != null){
            String status = score.getStatus();

            if("0".equals(status)){
                score.setStatus("1");
                scoreMapper.updateById(score);
            }

            if("2".equals(status)) throw new CommonException(ResultCode.EXAM_IS_COMMIT);
        }

        return Result.SUCCESS();
    }

    public Result getExamStatus(String id) {
        return new Result(ResultCode.SUCCESS,examMapper.selectById(id));
    }

    //{id:this.parentData.id,singleOptions:this.singleAskList.join(","),selectionOptions:this.checkboxAskList.join("@"),askOptions:arr.join("!&")};
    public Result saveTempAnswer(Map<String, Object> map) {
        String singleOptions = map.get("singleOptions").toString();
        String selectionOptions = map.get("selectionOptions").toString();
        String askOptions = map.get("askOptions").toString();
        String examId = map.get("id").toString();
        ProfileResult profileResult = (ProfileResult) SecurityUtils.getSubject().getPrincipal();
        String studentId = profileResult.getId();

        StringBuffer sb = new StringBuffer();

        sb.append(singleOptions).append("#").append(selectionOptions).append("#").append(askOptions);

        Boolean bool = redisTemplate.opsForHash().hasKey("temp_exam", examId + "#" + studentId);

        if(bool){
            redisTemplate.opsForHash().delete("temp_exam", examId + "#" + studentId);
        }
        redisTemplate.opsForHash().put("temp_exam", examId + "#" + studentId,sb.toString().trim());

        return Result.SUCCESS();
    }

    public Result echoTempAnswer(String examId) {

        ProfileResult profileResult = (ProfileResult) SecurityUtils.getSubject().getPrincipal();

        String studentId = profileResult.getId();

        String answer = "";

        if(redisTemplate.opsForHash().hasKey("temp_exam", examId + "#" + studentId)){
            answer = redisTemplate.opsForHash().get("temp_exam", examId + "#" + studentId).toString();
        }

        return new Result(ResultCode.SUCCESS,answer);
    }

}
