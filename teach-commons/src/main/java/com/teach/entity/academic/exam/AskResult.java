package com.teach.entity.academic.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@TableName("st_ask_result")
public class AskResult implements Serializable {

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    private String askAnswer;

    private String askId;

    private String examId;

    private String studentId;

    private String teacherComment;//老师评语

    private Integer score;  //老师评分: 问答题单题分数

    private String markingStatus; //是否已经评分 1 未评分 2已评
}
