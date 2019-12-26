package com.access.business.academic.question.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.access.business.academic.question.service.QuestionService;


import com.alibaba.fastjson.JSON;
import com.teach.aop.Log;
import com.teach.entity.academic.question.Single;
import com.teach.response.Result;


import com.teach.utils.PoiUtil;
import org.apache.poi.ss.usermodel.*;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/question")
public class QuestionController {


    @Autowired
    private QuestionService questionService;


    @Log("试题列表")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public Result list(@RequestBody Map<String,Object> map){
        return questionService.list(map);
    }

    @Log("新增试题")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result save(@RequestBody Map<String,Object> map) throws Exception {
        return questionService.save(map);
    }

    @Log("修改试题")
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

    @RequestMapping(value = "/exportData",method = RequestMethod.POST)
    public String exportExcel(HttpServletResponse response, @RequestBody Map<String,Object> map) throws Exception{


        return questionService.exportData(map,response);
    }

    @RequestMapping(value = "/uploadQuestion",method = RequestMethod.POST)
    public Result uploadQuestion(@RequestParam("file") MultipartFile file) throws IOException {
        return  questionService.uploadQuestion(file);
    }



}
