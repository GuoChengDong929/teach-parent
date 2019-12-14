package com.access.business.academic.curriculum.controller;

import com.access.business.academic.curriculum.service.CurriculumService;
import com.teach.entity.academic.curriculum.Curriculum;
import com.teach.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/curriculum")
@RestController
public class CurriculumController {

    @Autowired
    private CurriculumService curriculumService;

    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public Result findAll(){
        return curriculumService.findAll();
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result save(@RequestBody Curriculum curriculum){
        return curriculumService.save(curriculum);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public Result update(@RequestBody Curriculum curriculum){
        return curriculumService.update(curriculum);
    }

}
