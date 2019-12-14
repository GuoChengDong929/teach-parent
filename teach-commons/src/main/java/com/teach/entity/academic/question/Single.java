package com.teach.entity.academic.question;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@TableName("st_single")
public class Single implements Serializable {


    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    private String discipline; //学科编号  1软件  2网络 3通用

    private String chapterId; //章节id

    private String chapterName;//章节名称

    private String sourced; //来源 1课程试题 2面试宝典 3企业真题

    @TableField(strategy = FieldStrategy.IGNORED) //忽略null值判断  如果不设置, 无法通过mybatis plus 来给字段赋值Null值
    private String companyId; //企业id

    @TableField(strategy = FieldStrategy.IGNORED) //忽略null值判断  如果不设置, 无法通过mybatis plus 来给字段赋值Null值
    private String companyName; //企业名称

    private String type; //试题类型  1单选 2多选 3问答 4上机

    private String singleContent; //试题题干

    private String singleOptionA; //选项A

    private String singleOptionB; //选项B

    private String singleOptionC; //选项C

    private String singleOptionD; //选项D

    private String singleAsk; //单选题答案

    private String status; //单选题状态

    private Date createTime; //创建时间

}
