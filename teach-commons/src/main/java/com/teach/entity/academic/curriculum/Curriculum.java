package com.teach.entity.academic.curriculum;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@TableName("ac_curriculum")
public class Curriculum implements Serializable {


    @TableId(type = IdType.ID_WORKER_STR)
    private String id;
    private String name;

    @TableField("sort_number")
    private Integer sortNumber;

    @TableField("create_time")
    private Date createTime;

    private String title; //刷题专用: 显示标题文本

    private String description; //刷题专用: 显示阶段描述

    private String avatar; //刷题专用: 显示的图片地址

    private Date modifyTime;
    private String modifyUser;
    private String modifyId;

}
