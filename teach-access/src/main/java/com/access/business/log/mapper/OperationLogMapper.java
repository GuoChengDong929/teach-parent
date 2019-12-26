package com.access.business.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teach.entity.log.SysOperationLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogMapper extends BaseMapper<SysOperationLog> {
}
