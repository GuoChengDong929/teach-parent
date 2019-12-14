package com.teach.entity.academic.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("st_exam")
public class Exam implements Serializable {

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    private String examName;


    private Date examTime;

    private Integer examTimeLength;

    private String examType;

    private String examStatus;

    private String examPattern;

    private String questionTypeIds;

    private String chapterIds;


    private String singleJoins;
    private Integer singleCount;
    private Integer singleScore;

    private String selectionJoins;
    private Integer selectionCount;
    private Integer selectionScore;

    private String askJoins;
    private Integer askCount;
    private Integer askScore;

    private String upperJoins;
    private Integer upperCount;
    private Integer upperScore;


    private String classesId;

    private String classesName;

    private Integer personNumber;

    private Date createTime;



}

