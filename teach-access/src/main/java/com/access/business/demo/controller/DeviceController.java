package com.access.business.demo.controller;

import com.access.business.demo.service.DeviceService;
import com.teach.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @RequestMapping(value = "/getListByDevice", method = RequestMethod.POST)
    public Result getListByDevice(@RequestBody Map<String, Object> map) {
        return deviceService.getListByDevice(map);
    }

    @RequestMapping(value = "/deleteDevice/{id}",method = RequestMethod.DELETE)
    public Result deleteDevice(@PathVariable("id") String id){
        return deviceService.deleteDevice(id);
    }

}
