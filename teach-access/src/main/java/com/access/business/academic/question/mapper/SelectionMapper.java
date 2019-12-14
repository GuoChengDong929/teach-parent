package com.access.business.academic.question.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teach.entity.academic.question.Selection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SelectionMapper extends BaseMapper<Selection> {

    @Select("select chapter_name,sourced,company_name,selection_content,selection_option_a,selection_option_b,selection_option_c,selection_option_d from st_selection where id = #{selectionId}")
    Selection findByIdNoAnswer(@Param("selectionId") String selectionId);
}
