package com.access.business.academic.exam.service;

import com.access.business.academic.exam.mapper.AskResultMapper;
import com.access.business.academic.exam.mapper.ExamMapper;
import com.access.business.academic.exam.mapper.ScoreMapper;
import com.access.business.academic.question.mapper.AskMapper;
import com.access.business.academic.question.mapper.SelectionMapper;
import com.access.business.academic.question.mapper.SingleMapper;
import com.access.business.access.repository.UserRepository;
import com.access.business.quality.student.mapper.StudentMapper;
import com.access.business.quality.transact.mapper.ClassesMapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teach.base.BaseService;
import com.teach.entity.academic.exam.AskResult;
import com.teach.entity.academic.exam.Exam;
import com.teach.entity.academic.exam.Score;
import com.teach.entity.academic.question.Ask;
import com.teach.entity.academic.question.Selection;
import com.teach.entity.academic.question.Single;
import com.teach.entity.access.User;
import com.teach.entity.quality.student.Student;
import com.teach.entity.quality.transact.Classes;
import com.teach.entity.vo.AskResultVo;
import com.teach.entity.vo.AskVo;
import com.teach.entity.vo.QuestionTypeVo;
import com.teach.entity.vo.ScoreVo;
import com.teach.error.CommonException;
import com.teach.response.PageResult;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import com.teach.utils.BeanMapUtils;

import com.teach.utils.DateUtils;
import com.teach.utils.IdWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class ExamService extends BaseService {


    @Autowired
    private IdWorker idWorker;
    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private ClassesMapper classesMapper;

    @Autowired
    private SingleMapper singleMapper;

    @Autowired
    private SelectionMapper selectionMapper;

    @Autowired
    private AskMapper askMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private StudentMapper studentMapper;


    @Autowired
    private AskResultMapper askResultMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public Result list(Map<String, Object> map) {

        Integer page = Integer.parseInt(map.get("page").toString());
        Integer size = Integer.parseInt(map.get("size").toString());
        IPage<Exam> iPage = new Page<>(page,size);

        QueryWrapper<Exam> queryWrapper = new QueryWrapper<>();
        if(map.get("classesId") != null) queryWrapper.eq("classes_id",map.get("classesId").toString());
        if(map.get("examName") != null) queryWrapper.like("exam_name",map.get("examName").toString());
        if(map.get("examType") != null) queryWrapper.eq("exam_type",map.get("examType").toString());
        if(map.get("examStatus") != null) queryWrapper.eq("exam_status",map.get("examStatus").toString());

        if(map.get("questionType") != null && map.get("questionType").toString().equals("1")){
            queryWrapper.ne("question_type_ids","4");
        }else if (map.get("questionType") != null && map.get("questionType").toString().equals("2")){
            queryWrapper.eq("question_type_ids","4");
        }
        queryWrapper.orderByDesc("create_time");
        IPage<Exam> result = examMapper.selectPage(iPage, queryWrapper);
        PageResult<Exam> pageResult = new PageResult<>(result.getTotal(),result.getRecords());
        return new Result(ResultCode.SUCCESS,pageResult);
    }



    public Result save(Map<String, Object> map) throws Exception {


        String examTime = map.get("examTime").toString();

        Date parse = new SimpleDateFormat("yyyy-MM-dd").parse(examTime);


        if(parse.getTime() < (new Date().getTime() + 1000 * 60 * 10 )){
            return new Result(ResultCode.EXAM_TIME_LT_NEW_DATE);
        }

        map.put("examTime",parse);


        String examType = map.get("examType").toString();

        String classesId = map.get("classesId").toString();


        String questionTypeIds = map.get("questionTypeIds").toString();


        if("1".equals(examType)){
            if(questionTypeIds.contains("4")){
                Exam target = examMapper.getExamByUpperExamTypeAndClassesIdAndExamTime(examType,classesId,parse);
                if( target  != null ) throw new CommonException(ResultCode.EXAM_DAY_IS_ALWAYS);
            }else{
                Exam targetExam = examMapper.getExamByExamTypeAndClassesIdAndExamTime(examType,classesId,parse);
                if(targetExam != null) throw new CommonException(ResultCode.EXAM_DAY_IS_ALWAYS);
            }
        }else if("2".equals(examType)){
            String timeInterval = DateUtils.getTimeInterval(parse);
            String[] split = timeInterval.split(",");
            Date start = new SimpleDateFormat("yyyy-MM-dd").parse(split[0]);
            Date end = new SimpleDateFormat("yyyy-MM-dd").parse(split[1]);

            if(questionTypeIds.contains("4")){
                List<Exam> targetExam = examMapper.getWeekUpperExamByExamTypeAndClassesIdAndStartAndEnd(examType,classesId,start,end);
                if(targetExam != null && targetExam.size() == 1) throw new CommonException(ResultCode.EXAM_WEEK_IS_ALWAYS);
            }else{
                List<Exam> targetExam = examMapper.getWeekExamByExamTypeAndClassesIdAndStartAndEnd(examType,classesId,start,end);
                if(targetExam != null && targetExam.size() == 1 ) throw new CommonException(ResultCode.EXAM_WEEK_IS_ALWAYS);
            }

        }else if("3".equals(examType)){
            Date start = DateUtils.getFirstDayDateOfMonth(parse);
            Date end = DateUtils.getLastDayOfMonth(parse);
            if(questionTypeIds.contains("4")){
                List<Exam> targetExam = examMapper.getWeekUpperExamByExamTypeAndClassesIdAndStartAndEnd(examType,classesId,start,end);
                if(targetExam != null && targetExam.size() == 1) throw new CommonException(ResultCode.EXAM_MONTH_IS_ALWAYS);
            }else{
                List<Exam> targetExam = examMapper.getWeekExamByExamTypeAndClassesIdAndStartAndEnd(examType,classesId,start,end);
                if(targetExam != null && targetExam.size() == 1) throw new CommonException(ResultCode.EXAM_MONTH_IS_ALWAYS);
            }
        }



        Object singleScore = map.get("singleScore");

        if(singleScore != null) map.put("singleScore",Integer.parseInt(singleScore.toString()));

        Object selectionScore = map.get("selectionScore");

        if(selectionScore != null) map.put("selectionScore",Integer.parseInt(selectionScore.toString()));

        Object askScore = map.get("askScore");

        if(askScore != null) map.put("askScore",Integer.parseInt(askScore.toString()));

        Exam exam = BeanMapUtils.mapToBean(map, Exam.class);



        String examPattern = exam.getExamPattern();

        if("2".equals(examPattern)){ //自动选题
            //String questionTypeIds = exam.getQuestionTypeIds();
            String chapterIds = exam.getChapterIds();

            List<Single> singleList = new ArrayList<>();
            List<Selection> selectionList = new ArrayList<>();
            List<Ask> askList = new ArrayList<>();

            for (String chapterId : chapterIds.split(",")) {

                QueryWrapper<Single> singleQueryWrapper = new QueryWrapper<>();
                singleQueryWrapper.eq("chapter_id",chapterId);

                List<Single> querySingleList = singleMapper.selectList(singleQueryWrapper);


                if(querySingleList != null && querySingleList.size() > 0){
                    for (Single single : querySingleList) {
                        singleList.add(single);
                    }
                }


                QueryWrapper<Selection> selectionQueryWrapper = new QueryWrapper<>();
                selectionQueryWrapper.eq("chapter_id",chapterId);
                List<Selection> querySelectionList = selectionMapper.selectList(selectionQueryWrapper);

                if(querySelectionList != null && querySelectionList.size() > 0){
                    for (Selection selection : querySelectionList) {
                        selectionList.add(selection);
                    }
                }

                QueryWrapper<Ask> askQueryWrapper = new QueryWrapper<>();
                askQueryWrapper.eq("chapter_id",chapterId);
                List<Ask> queryAskList = askMapper.selectList(askQueryWrapper);
                if(queryAskList != null && queryAskList.size() > 0){
                    for (Ask ask : queryAskList) {
                        askList.add(ask);
                    }
                }

            }


            if(questionTypeIds.contains("1")){

                Integer singleCount = exam.getSingleCount();

                if(singleList.size() < singleCount){
                    throw new CommonException(ResultCode.SINGLE_SIZE_OUTPUT);
                }

                Collections.shuffle(singleList,new Random(3)); //打乱3次

                List<Single> singles = singleList.subList(0, singleCount);

                List<String> singleIds = new ArrayList<>();

                for (Single single : singles) {
                    singleIds.add(single.getId());
                }

                String singleJoin = StringUtils.join(singleIds, ",");

                exam.setSingleJoins(singleJoin);
            }

            if(questionTypeIds.contains("2")){

                Integer selectionCount = exam.getSelectionCount();

                if(selectionList.size() < selectionCount){
                    throw new CommonException(ResultCode.SELECTION_SIZE_OUTPUT);
                }

                Collections.shuffle(selectionList,new Random(3));

                List<Selection> selections = selectionList.subList(0, selectionCount);

                List<String> selectionIds = new ArrayList<>();

                for (Selection selection : selections) {
                    selectionIds.add(selection.getId());
                }

                String selectionJoin = StringUtils.join(selectionIds, ",");

                exam.setSelectionJoins(selectionJoin);

            }

            if(questionTypeIds.contains("3")){

                Integer askCount = exam.getAskCount();

                if(askList.size() < askCount){
                    throw new CommonException(ResultCode.ASK_SIZE_OUTPUT);
                }


                Collections.shuffle(askList,new Random(3));

                List<Ask> asks = askList.subList(0, askCount);

                List<String> askIds = new ArrayList<>();

                for (Ask ask : asks) {
                    askIds.add(ask.getId());
                }

                String askJoin = StringUtils.join(askIds, ",");

                exam.setAskJoins(askJoin);

            }
        }


        Classes classes = classesMapper.selectById(exam.getClassesId());

        exam.setId(idWorker.nextId() + "");
        exam.setPersonNumber(classes.getPersonNumber());
        exam.setClassesName(classes.getClassesName());
        exam.setExamStatus("1"); //未开始


        exam.setCreateTime(new Date());


        exam.setModifyId(currentUser().getId());
        exam.setModifyUser(currentUser().getNickName());
        exam.setModifyTime(new Date());

        examMapper.insert(exam);

        return Result.SUCCESS();
    }



    public Result findExamByDBHasAnswer(Exam exam) {
        QuestionTypeVo vo = new QuestionTypeVo();

        List<Single> singles = new ArrayList<>();
        List<Selection> selections = new ArrayList<>();
        List<Ask> asks = new ArrayList<>();

        String questionTypeIds = exam.getQuestionTypeIds();

        if(questionTypeIds.contains("1")){
            String singleJoins = exam.getSingleJoins();
            for (String singleId : singleJoins.split(",")) {
                Single single = singleMapper.selectById(singleId);
                singles.add(single);
            }
        }

        if(questionTypeIds.contains("2")){
            String selectionJoins = exam.getSelectionJoins();
            for (String selectionId : selectionJoins.split(",")) {
                Selection selection = selectionMapper.selectById(selectionId);
                selections.add(selection);
            }
        }

        if(questionTypeIds.contains("3")){
            String askJoins = exam.getAskJoins();
            for (String askId : askJoins.split(",")) {
                Ask ask = askMapper.selectById(askId);
                asks.add(ask);
            }
        }

        vo.setAskList(asks);
        vo.setSelectionList(selections);
        vo.setSingleList(singles);

        return new Result(ResultCode.SUCCESS,vo);
    }


    public Result start(Exam exam) {
        exam.setExamStatus("2");//状态为2 进行中

        exam.setModifyId(currentUser().getId());
        exam.setModifyUser(currentUser().getNickName());
        exam.setModifyTime(new Date());
        examMapper.updateById(exam);


        //把该试卷的所有试题生成一个试卷,存入到redis中

        QuestionTypeVo vo = new QuestionTypeVo();

        List<Single> singles = new ArrayList<>();
        List<Selection> selections = new ArrayList<>();
        List<Ask> asks = new ArrayList<>();

        String questionTypeIds = exam.getQuestionTypeIds();

        if(questionTypeIds.contains("1")){
            String singleJoins = exam.getSingleJoins();
            for (String singleId : singleJoins.split(",")) {
                Single single = singleMapper.findByIdNoAnswer(singleId);
                singles.add(single);
            }
        }

        if(questionTypeIds.contains("2")){
            String selectionJoins = exam.getSelectionJoins();
            for (String selectionId : selectionJoins.split(",")) {
                Selection selection = selectionMapper.findByIdNoAnswer(selectionId);
                selections.add(selection);
            }
        }

        if(questionTypeIds.contains("3")){
            String askJoins = exam.getAskJoins();
            for (String askId : askJoins.split(",")) {
                Ask ask = askMapper.findByIdNoAnswer(askId);
                asks.add(ask);
            }
        }

        vo.setAskList(asks);
        vo.setSelectionList(selections);
        vo.setSingleList(singles);


        redisTemplate.opsForHash().put("exam",exam.getId(), JSON.toJSONString(vo));


        //初始化所有学生当前试卷的score成绩
        //找到当前试卷下的班级中所有的学生
        String classesId = exam.getClassesId();

        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("classes_id",classesId);

        List<Student> students = studentMapper.selectList(queryWrapper);

        if(students != null && students.size() > 0){
            for (Student student : students) {
                Score score = Score.builder()
                        .executeTime(new Date())
                        .studentId(student.getId())
                        .score(0)
                        .examId(exam.getId())
                        .askScore(0)
                        .upperScore(0)
                        .singleScore(0)
                        .selectionScore(0)
                        .singleErr(0)
                        .singleSucc(0)
                        .selectionErr(0)
                        .selectionSucc(0)
                        .singleSuccIds("")
                        .singleErrIds("")
                        .selectionSuccIds("")
                        .selectionErrIds("")
                        .status("0") //0初始化 未交卷1 已交卷2
                .build();
                scoreMapper.insert(score);
            }
        }

        //如果有问答题 初始化学生问答题结果  st_ask_result
        if(questionTypeIds.contains("3")){
            String askJoins = exam.getAskJoins();


            for (Student student : students) {
                for (String askId : askJoins.split(",")) {
                    AskResult askResult = AskResult.builder()
                            .askId(askId)
                            .examId(exam.getId())
                            .studentId(student.getId())
                            .score(0)
                            .markingStatus("1")
                            .askAnswer("暂无")
                            .teacherComment("暂无")
                            .build();
                    askResultMapper.insert(askResult);
                }
            }

        }


        return Result.SUCCESS();
    }

    public Result findById(String id) {
        Exam exam = examMapper.selectById(id);
        if("1".equals(exam.getExamStatus())){
            return Result.SUCCESS();
        }else{
            return Result.FAIL();
        }
    }

    public Result findExamByCache(String id) {

        Boolean bool = redisTemplate.opsForHash().hasKey("exam", id);

        if(!bool){
            return new Result(ResultCode.FAIL);
        }

        Object exam = redisTemplate.opsForHash().get("exam", id);

        if(exam != null){
            QuestionTypeVo questionTypeVo = JSON.parseObject(exam.toString(), QuestionTypeVo.class);

            return new Result(ResultCode.SUCCESS,questionTypeVo);
        }else{
            return new Result(ResultCode.FAIL);
        }
    }




    public Result getStudentListByExam(Exam exam) {
        QueryWrapper<Score> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("exam_id",exam.getId());

        List<Score> scores = scoreMapper.selectList(queryWrapper);

        if(scores != null && scores.size() > 0){
            for (Score score : scores) {
                User user = userRepository.findById(score.getStudentId()).get();
                score.setStudentName(user.getNickName());
            }
        }
        return new Result(ResultCode.SUCCESS,scores);
    }

    public Result stop(Exam exam) {

        String questionTypeIds = exam.getQuestionTypeIds();

        //判断当前试卷是否有问题和上机题 如果没有 则试卷直接进入结束
        if(!questionTypeIds.contains("3") && !questionTypeIds.contains("4")){
            exam.setExamStatus("4"); //试卷修改为结束状态

            //TODO: start 后加的 不知道对不对  只有单选,多选, 直接结束试卷时,修改学生的状态  从1 改成 2 , 0不变
            String classesId = exam.getClassesId();

            QueryWrapper<Student> queryWrapper = new QueryWrapper<>();

            queryWrapper.eq("classes_id",classesId);

            List<Student> students = studentMapper.selectList(queryWrapper);

            for (Student student : students) {

                QueryWrapper<Score> scoreQueryWrapper = new QueryWrapper<>();

                scoreQueryWrapper.eq("student_id",student.getId());
                scoreQueryWrapper.eq("exam_id",exam.getId());

                Score score = scoreMapper.selectOne(scoreQueryWrapper);

                if("1".equals(score.getStatus())){
                    score.setStatus("2");
                    score.setModifyId(currentUser().getId());
                    score.setModifyUser(currentUser().getNickName());
                    score.setModifyTime(new Date());
                    scoreMapper.updateById(score);
                }
            }
            //TODO: end 后加的 不知道对不对  只有单选,多选, 直接结束试卷时,修改学生的状态  从1 改成 2 , 0不变
        }

        //判断当前试卷是否有问题或上机题 如果没有 则试卷直接进入结束
        if(questionTypeIds.contains("3") || questionTypeIds.contains("4")){
            exam.setExamStatus("3"); //试卷修改为批阅中
        }

        exam.setModifyId(currentUser().getId());
        exam.setModifyUser(currentUser().getNickName());
        exam.setModifyTime(new Date());

        examMapper.updateById(exam);

        return Result.SUCCESS();
    }

    public Result analysisExam(Exam exam) {
        List<ScoreVo> vo = examMapper.getStudentInfoByExamId(exam.getId());
        return new Result(ResultCode.SUCCESS,vo);
    }

    public Result markingStudentAsks(Exam exam) {

        //找到当前试卷下的班级中所有的学生
        String classesId = exam.getClassesId();

        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("classes_id",classesId);

        List<Student> students = studentMapper.selectList(queryWrapper);

        String askJoins = exam.getAskJoins();

        List<AskVo> result = new ArrayList<>();

        for (Student student : students) {
            AskVo askVo = new AskVo();
            User user = userRepository.findById(student.getId()).get();
            askVo.setStudent(student);
            askVo.setUser(user);

            QueryWrapper<Score> scoreQueryWrapper = new QueryWrapper<>();
            scoreQueryWrapper.eq("student_id",student.getId());
            scoreQueryWrapper.eq("exam_id",exam.getId());

            Score score = scoreMapper.selectOne(scoreQueryWrapper);

            askVo.setScore(score);


            //装载单个学生多个简答题答案
            List<AskResult> list = new ArrayList<>();

            for (String askId : askJoins.split(",")) {
                QueryWrapper<AskResult> askResultQueryWrapper = new QueryWrapper<>();
                askResultQueryWrapper.eq("ask_id",askId);
                askResultQueryWrapper.eq("exam_id",exam.getId());
                askResultQueryWrapper.eq("student_id",student.getId());
                AskResult askResult = askResultMapper.selectOne(askResultQueryWrapper);
                list.add(askResult);
            }

            askVo.setList(list);

            result.add(askVo);

        }

        List<Ask> asks = new ArrayList<>();

        for (String askId : askJoins.split(",")) {
            Ask ask = askMapper.selectById(askId);
            asks.add(ask);
        }


        AskResultVo vo = new AskResultVo();
        vo.setAsks(asks);
        vo.setList(result);

        return new Result(ResultCode.SUCCESS,vo);
    }

    /**

     key:askId=1203702450352553984,value:null
     key:examId=1205127949960413184,value:null
     key:studentId=1204954054976454656,value:null
     key:teacherComment=很好,很不错,value:null
     key:score=12,value:null
     key:scoreId=1205127989223292929,value:null
     */
    public Result updateStudentAskScore(Map<String, Object> map) {

        String askId = map.get("askId").toString();
        String examId = map.get("examId").toString();
        String studentId = map.get("studentId").toString();
        String scoreId = map.get("scoreId").toString();

        Integer score = Integer.parseInt(map.get("score").toString());

        if(map.get("teacherComment") == null || "".equals(map.get("teacherComment"))){
            return new Result(ResultCode.EXAM_MARKING_TEACHER_COMMENT);
        }


        Exam exam = examMapper.selectById(examId);

        Integer askScore = exam.getAskScore();

        if(score > askScore){
            return new Result(ResultCode.EXAM_MARKING_ASK_SCORE_MAX);
        }


        if(score < 0){
            return new Result(ResultCode.EXAM_MARKING_ASK_SCORE);
        }

        String teacherComment = map.get("teacherComment").toString();


        QueryWrapper<AskResult> askResultQueryWrapper = new QueryWrapper<>();
        askResultQueryWrapper.eq("ask_id",askId);
        askResultQueryWrapper.eq("exam_id",examId);
        askResultQueryWrapper.eq("student_id",studentId);


        AskResult askResult = askResultMapper.selectOne(askResultQueryWrapper);

        askResult.setMarkingStatus("2");
        askResult.setScore(score);
        askResult.setTeacherComment(teacherComment);

        askResultMapper.updateById(askResult);

        Score studentScore = scoreMapper.selectById(scoreId);

        studentScore.setAskScore(studentScore.getAskScore() + score);

        studentScore.setScore(studentScore.getScore() + score);

        studentScore.setModifyId(currentUser().getId());
        studentScore.setModifyUser(currentUser().getNickName());
        studentScore.setModifyTime(new Date());

        scoreMapper.updateById(studentScore);

        return Result.SUCCESS();
    }

    public Result getStudentAskResult(Map<String, Object> map) {

        String askId = map.get("askId").toString();
        String examId = map.get("examId").toString();
        String studentId = map.get("studentId").toString();

        QueryWrapper<AskResult> askResultQueryWrapper = new QueryWrapper<>();
        askResultQueryWrapper.eq("ask_id",askId);
        askResultQueryWrapper.eq("exam_id",examId);
        askResultQueryWrapper.eq("student_id",studentId);


        AskResult askResult = askResultMapper.selectOne(askResultQueryWrapper);

        String markingStatus = askResult.getMarkingStatus();

        if("1".equals(markingStatus)){
            return Result.SUCCESS();
        }else{
            return Result.FAIL();
        }

    }

    public Result endExam(Exam exam) {

        exam.setExamStatus("4");

        exam.setModifyId(currentUser().getId());
        exam.setModifyUser(currentUser().getNickName());
        exam.setModifyTime(new Date());

        examMapper.updateById(exam);

        //找到这个试卷的这个班级中所有的学生
        String classesId = exam.getClassesId();

        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("classes_id",classesId);

        List<Student> students = studentMapper.selectList(queryWrapper);

        for (Student student : students) {

            QueryWrapper<Score> scoreQueryWrapper = new QueryWrapper<>();

            scoreQueryWrapper.eq("student_id",student.getId());
            scoreQueryWrapper.eq("exam_id",exam.getId());

            Score score = scoreMapper.selectOne(scoreQueryWrapper);

            //TODO: start 后加的 不知道对不对  批阅后,修改学生的状态  从1 改成 2 , 0不变
            if("1".equals(score.getStatus())){
                score.setStatus("2");
                score.setModifyId(currentUser().getId());
                score.setModifyUser(currentUser().getNickName());
                score.setModifyTime(new Date());
                scoreMapper.updateById(score);
            }
            //TODO: end 后加的 不知道对不对  批阅后,修改学生的状态  从1 改成 2 , 0不变
        }

        return Result.SUCCESS();
    }
}
