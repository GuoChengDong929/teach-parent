package com.access.business.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teach.entity.academic.exam.Exam;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentExamMapper extends BaseMapper<Exam> {
}
