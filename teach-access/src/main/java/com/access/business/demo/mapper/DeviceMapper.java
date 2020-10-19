package com.access.business.demo.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teach.entity.demo.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeviceMapper extends BaseMapper<Device> {

    @Select("<script> " +
            "SELECT COUNT(0) " +
            "FROM device " +
            " </script>")
    long getDeviceTotal();

    @Select("<script> " +
            "SELECT d.* " +
            "FROM device d " +
            "LIMIT #{i},#{size} " +
            " </script>")
    List<Device> getDeviceList(int i, Integer size, Map<String, Object> map);
}
