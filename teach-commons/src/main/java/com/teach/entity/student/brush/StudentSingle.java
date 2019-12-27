package com.teach.entity.student.brush;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 刷题: 学生是否已答题
 * @Author 韩雪松
 * @Date 2019/12/26
 **/
@Data
@TableName("st_student_single")
public class StudentSingle implements Serializable {

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;
    private String studentId;
    private String chapterId;
    private String singleId;
}
