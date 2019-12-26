package com.access.business.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teach.entity.log.SysErrorLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ErrorLogMapper extends BaseMapper<SysErrorLog> {
}
