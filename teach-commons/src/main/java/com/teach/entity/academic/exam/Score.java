package com.teach.entity.academic.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("st_score")
public class Score implements Serializable {

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    private Integer singleSucc;

    private String singleSuccIds;

    private Integer singleErr;

    private String singleErrIds;

    private Integer selectionSucc;

    private String selectionSuccIds;

    private Integer selectionErr;

    private String selectionErrIds;

    private Integer singleScore;

    private Integer selectionScore;

    private Integer askScore;

    private Integer upperScore;

    private Integer score;

    private String status; //0 为初始化 1未交卷  2已交卷

    private String studentId;

    private String examId;

    private Date executeTime;

    @TableField(exist = false)
    private String studentName;


}
