package com.teach.entity.academic.question;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@TableName("st_ask")
public class Ask implements Serializable {

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    private String discipline; //学科编号  1软件  2网络 3通用

    private String chapterId; //章节id  对应表 chapter_id

    private String chapterName;//章节名称

    private String sourced; //来源 1课程试题 2面试宝典 3企业真题

    @TableField(strategy = FieldStrategy.IGNORED) //忽略null值判断  如果不设置, 无法通过mybatis plus 来给字段赋值Null值
    private String companyId; //企业id

    @TableField(strategy = FieldStrategy.IGNORED) //忽略null值判断  如果不设置, 无法通过mybatis plus 来给字段赋值Null值
    private String companyName; //企业名称

    private String type; //试题类型  1单选 2多选 3问答 4上机

    private String askContent; //问答题题干

    private String askAnalysis; //问答题解析

    private String status; //问答题状态

    private Date createTime;

    private Date modifyTime;
    private String modifyUser;
    private String modifyId;

}
