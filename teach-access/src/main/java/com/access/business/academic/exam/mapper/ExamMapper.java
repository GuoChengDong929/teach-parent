package com.access.business.academic.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teach.entity.academic.exam.Exam;
import com.teach.entity.quality.student.Student;
import com.teach.entity.vo.ScoreVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface ExamMapper extends BaseMapper<Exam> {

    @Select("<script>select  " +
            "       u.nick_name as nickName,s.id as studentId,  " +
            "       sc.status,sc.score,sc.single_succ as singleSucc, " +
            "       sc.single_score as singleScore, sc.single_succ_ids as singleSuccIds,sc.single_err_ids as singleErrIds, " +
            "       sc.selection_succ_ids as selectionSuccIds, sc.selection_err_ids as selectionErrIds," +
            "       sc.single_err as singleErr,sc.selection_succ as selectionSucc, sc.selection_err as selectionErr,sc.selection_score as selectionScore, " +
            "       e.exam_name as examName  " +
            "from pe_user u,qu_student s,st_score sc,st_exam e  " +
            "where e.id = sc.exam_id " +
            "    and sc.student_id = s.id " +
            "    and s.id = u.id " +
            "    and e.id = #{examId} " +
            "order by sc.score DESC</script>")
    List<ScoreVo> getStudentInfoByExamId(@Param("examId")String examId);





    @Select("SELECT e.* " +
            "FROM st_exam e " +
            "WHERE e.`classes_id` = #{classesId} " +
            "AND e.`exam_type` = #{examType} " +
            "AND e.`exam_time` = #{examTime} " +
            "AND e.question_type_ids = '4'" )
    Exam getExamByUpperExamTypeAndClassesIdAndExamTime(@Param("examType")String examType, @Param("classesId")String classesId, @Param("examTime")Date examTime);

    @Select("SELECT e.* " +
            "FROM st_exam e " +
            "WHERE e.`classes_id` = #{classesId} " +
            "AND e.`exam_type` = #{examType} " +
            "AND e.`exam_time` = #{examTime} " +
            "AND e.question_type_ids != '4'" )
    Exam getExamByExamTypeAndClassesIdAndExamTime(@Param("examType")String examType, @Param("classesId")String classesId, @Param("examTime") Date examTime);

    @Select("SELECT e.* " +
            "FROM st_exam e " +
            "WHERE e.`classes_id` = #{classesId} " +
            "AND e.`exam_type` = #{examType} " +
            "AND e.`exam_time` BETWEEN #{start} AND #{end} " +
            "AND e.question_type_ids != '4'" )
    List<Exam> getWeekExamByExamTypeAndClassesIdAndStartAndEnd(
            @Param("examType")String examType,
            @Param("classesId")String classesId,
            @Param("start")Date start,
            @Param("end")Date end);

    @Select("SELECT e.* " +
            "FROM st_exam e " +
            "WHERE e.`classes_id` = #{classesId} " +
            "AND e.`exam_type` = #{examType} " +
            "AND e.`exam_time` BETWEEN #{start} AND #{end} " +
            "AND e.question_type_ids = '4'" )
    List<Exam> getWeekUpperExamByExamTypeAndClassesIdAndStartAndEnd(
            @Param("examType")String examType,
            @Param("classesId")String classesId,
            @Param("start")Date start,
            @Param("end")Date end);
}
