package com.access.business.academic.question.controller;

import com.access.business.academic.question.service.QuestionService;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/question")
public class QuestionController {


    @Autowired
    private QuestionService questionService;

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public Result list(@RequestBody Map<String,Object> map){
        return questionService.list(map);
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result save(@RequestBody Map<String,Object> map) throws Exception {
        return questionService.save(map);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public Result update(@RequestBody Map<String,Object> map) throws Exception {
        return questionService.update(map);
    }

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Result upload(@RequestParam("file") MultipartFile file) throws IOException {
        return questionService.upload(file.getOriginalFilename(),file.getInputStream());
    }

    @RequestMapping(value = "/getQuestionsByChapterIdsAndQuestionTypeIds",method = RequestMethod.POST)
    public Result getQuestionsByChapterIdsAndQuestionTypeIds(@RequestBody Map<String,Object> map){
        return questionService.getQuestionsByChapterIdsAndQuestionTypeIds(map);
    }
}
