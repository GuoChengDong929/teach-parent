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
@TableName("st_selection")
public class Selection implements Serializable {

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

    private String selectionContent;

    private String selectionOptionA;
    private String selectionOptionB;
    private String selectionOptionC;
    private String selectionOptionD;

    private String selectionAsk;

    @TableField(exist = false)
    private String [] selectionAskArray; //用于前台复选框数据回显

    private String status;

    private Date createTime;

    private Date modifyTime;
    private String modifyUser;
    private String modifyId;
}
