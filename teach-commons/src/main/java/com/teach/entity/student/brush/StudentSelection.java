package com.teach.entity.student.brush;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author 韩雪松
 * @Date 2019/12/26
 **/
@Data
@TableName("st_student_selection")
public class StudentSelection implements Serializable {

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;
    private String studentId;
    private String chapterId;
    private String selectionId;
}

