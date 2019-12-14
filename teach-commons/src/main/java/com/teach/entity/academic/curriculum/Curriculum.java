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
}