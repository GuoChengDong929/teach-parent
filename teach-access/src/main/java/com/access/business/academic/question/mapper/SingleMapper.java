package com.access.business.academic.question.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teach.entity.academic.question.Single;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SingleMapper extends BaseMapper<Single> {
    @Select("select chapter_name,sourced,company_name,single_content,single_option_a,single_option_b,single_option_c,single_option_d  from st_single where id = #{singleId}")
    Single findByIdNoAnswer(@Param("singleId") String singleId);
}
