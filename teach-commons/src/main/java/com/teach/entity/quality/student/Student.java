package com.teach.entity.quality.student;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("qu_student")
public class Student implements Serializable {


    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    private String studyType;   //状态:1在读 2休学 3退学 4毕业

    private Date stopStudyTime;   //休学时间

    private Integer stopStudyLength;    //休学时长

    private String stopStudyReason;   //休学原因

    private Date backStudyTime;    //退学时间

    private String backStudyReason; //退学原因

    private Date finishStudyTime;//毕业时间

    private String classesId;

    private String classesName;
}
