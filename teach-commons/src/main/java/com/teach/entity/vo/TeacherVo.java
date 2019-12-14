package com.teach.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class TeacherVo {

    List<UserTeacherVo> zjTeacher;
    List<UserTeacherVo> jyTeacher;
    List<UserTeacherVo> bzrTeacher;
}
