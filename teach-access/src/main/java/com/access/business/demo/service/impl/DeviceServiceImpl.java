package com.access.business.demo.service.impl;

import com.access.business.demo.mapper.DeviceMapper;
import com.access.business.demo.service.DeviceService;
import com.teach.entity.demo.Device;
import com.teach.response.PageResult;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public Result getListByDevice(Map<String, Object> map) {
        long total = deviceMapper.getDeviceTotal();
        Integer page = Integer.parseInt(map.get("page").toString());
        Integer size = Integer.parseInt(map.get("size").toString());
        List<Device> list = deviceMapper.getDeviceList((page - 1) * size,size,map);
        PageResult<Device> pageResult = new PageResult<>(total,list);
        return new Result(ResultCode.SUCCESS,pageResult);
    }

    @Override
    public Result deleteDevice(String id) {
        deviceMapper.deleteById(id);
        return Result.SUCCESS();
    }
}
