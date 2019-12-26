package com.teach.entity.quality.transact;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.teach.entity.access.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("qu_classes")
@ToString
public class Classes implements Serializable {

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    private String classesName;

    private Date startTime;

    private Date endTime;

    private String curriculumId;

    private String curriculumName;


    private Integer personNumber;

    private String teachers;

    private Date createTime;


    @TableField(exist = false)
    List<User> users;

    private String zjId;

    private String bzrId;

    private String jyId;

    private String invalid; // 0作废 1不作废

    private Date modifyTime;
    private String modifyUser;
    private String modifyId;
}
