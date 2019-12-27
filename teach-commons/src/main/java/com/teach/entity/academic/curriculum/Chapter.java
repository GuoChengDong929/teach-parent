package com.teach.entity.academic.curriculum;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.teach.entity.academic.question.Ask;
import com.teach.entity.academic.question.Selection;
import com.teach.entity.academic.question.Single;
import com.teach.entity.academic.question.Upper;
import lombok.*;


import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@TableName("ac_chapter")
public class Chapter implements Serializable {
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    private String name;

    @TableField("sort_number")
    private Integer sortNumber;

    @TableField("curriculum_id")
    private String curriculumId; //课程id

    @TableField("create_time")
    private Date createTime;





    @TableField(exist = false)
    private Integer singleCount;

    @TableField(exist = false)
    private Integer selectionCount;

    @TableField(exist = false)
    private Integer askCount;

    @TableField(exist = false)
    private Integer upperCount;

    @TableField(exist = false)
    private Integer total;

    @TableField(exist = false)
    private Integer okCount; //已答题数量


    private Date modifyTime;
    private String modifyUser;
    private String modifyId;

}
