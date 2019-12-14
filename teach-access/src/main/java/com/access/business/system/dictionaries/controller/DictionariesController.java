package com.access.business.system.dictionaries.controller;

import com.access.business.system.dictionaries.service.DictionariesService;
import com.teach.entity.system.dictionaries.Dictionaries;
import com.teach.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/dictionaries")
@RestController
public class DictionariesController {

    @Autowired
    private DictionariesService dictionariesService;

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public Result list(@RequestBody Map<String,Object> map){
        return dictionariesService.list(map);
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result save(@RequestBody Dictionaries dictionaries){
        return dictionariesService.save(dictionaries);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public Result update(@RequestBody Dictionaries dictionaries){
        return dictionariesService.update(dictionaries);
    }

    @RequestMapping(value = "/getDictionariesByCode",method = RequestMethod.POST)
    public Result getDictionariesByCode(@RequestBody Map<String,Object> map){
        return dictionariesService.getDictionariesByCode(map);
    }
}
