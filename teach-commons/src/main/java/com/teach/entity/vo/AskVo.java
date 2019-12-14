package com.teach.entity.vo;

import com.teach.entity.academic.exam.AskResult;
import com.teach.entity.academic.exam.Score;
import com.teach.entity.access.User;
import com.teach.entity.quality.student.Student;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AskVo implements Serializable {

    private Student student;
    private User user;
    private List<AskResult> list;
    private Score score;
}
