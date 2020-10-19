package com.access.business.demo.service;

import com.teach.response.Result;

import java.util.Map;

public interface DeviceService {
    Result getListByDevice(Map<String, Object> map);

    Result deleteDevice(String id);
}
