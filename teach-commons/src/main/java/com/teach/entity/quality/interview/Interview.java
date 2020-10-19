package com.teach.entity.quality.interview;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("interview")
public class Interview implements Serializable {

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    @TableField("create_time")
    private Date createTime;  //创建时间

    @TableField("interview_address")
    private String interviewAddress;   //访谈地点

    @TableField("interview_objective")
    private String interviewObjective;     //访谈目的

    @TableField("interview_content")
    private String interviewContent;     //访谈内容

    @TableField("interview_results")
    private String interviewResults;     //访谈结果

    @TableField("teacher_id")
    private String teacherId;     //访谈人

    @TableField("student_id")
    private String studentId;   //被访谈人

    @TableField("classes_id")
    private String classesId;

    private transient String studentName;

    private transient String classesName;

}
