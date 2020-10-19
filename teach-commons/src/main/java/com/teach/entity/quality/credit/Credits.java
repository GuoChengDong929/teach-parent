package com.teach.entity.quality.credit;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
@TableName("credit")
public class Credits implements Serializable {

//    @TableId(type= IdType.ID_WORKER_STR)
//    private String id;
//
//    @TableField("grade")
//    private Integer grade;
//
//    @TableField("mark")
//    private String mark;   //0初始化分数   1减分  2加分
//
//    @TableField("cause")
//    private String cause;
//
//    @TableField("create_time")
//    private Date createTime;
//
//    @TableField("student_id")
//    private String studentId;
//
//    @TableField("classes_id")
//    private String classesId;
//
//    private transient String studentName;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String fraction;    //单次分数

    private String mark;        //0初始化 1加分 2减分

    private String incident;    //事由

    @TableField("create_time")
    private Date createTime;

    @TableField("student_id")
    private String studentId;

}
