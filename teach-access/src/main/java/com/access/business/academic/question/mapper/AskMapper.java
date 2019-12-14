package com.access.business.academic.question.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teach.entity.academic.question.Ask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AskMapper extends BaseMapper<Ask> {

    @Select("select chapter_name,sourced,company_name,ask_content from st_ask where id = #{askId}")
    Ask findByIdNoAnswer(@Param("askId") String askId);
}
