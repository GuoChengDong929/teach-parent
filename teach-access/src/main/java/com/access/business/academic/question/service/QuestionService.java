package com.access.business.academic.question.service;

import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import cn.hutool.system.SystemUtil;
import com.access.business.academic.curriculum.mapper.ChapterMapper;
import com.access.business.academic.question.mapper.AskMapper;
import com.access.business.academic.question.mapper.SelectionMapper;
import com.access.business.academic.question.mapper.SingleMapper;
import com.access.business.academic.question.mapper.UpperMapper;
import com.access.business.work.company.mapper.CompanyMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teach.base.BaseService;
import com.teach.entity.academic.curriculum.Chapter;
import com.teach.entity.academic.question.Ask;
import com.teach.entity.academic.question.Selection;
import com.teach.entity.academic.question.Single;
import com.teach.entity.academic.question.Upper;
import com.teach.entity.vo.QuestionTypeVo;
import com.teach.entity.work.Company;
import com.teach.error.CommonException;
import com.teach.response.PageResult;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import com.teach.utils.BeanMapUtils;
import com.teach.utils.IdWorker;
import com.teach.utils.PoiUtil;
import com.teach.utils.SftpUtil;
import io.netty.util.internal.MacAddressUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
@SuppressWarnings("all")
public class QuestionService extends BaseService {

    @Autowired
    private SingleMapper singleMapper;

    @Autowired
    private SelectionMapper selectionMapper;

    @Autowired
    private AskMapper askMapper;

    @Autowired
    private UpperMapper upperMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private IdWorker idWorker;

    public Result list(Map<String, Object> map) {

        Integer page = Integer.parseInt(map.get("page").toString());
        Integer size = Integer.parseInt(map.get("size").toString());

        String type = map.get("type").toString();

        /** 可选通用条件 **/

        Object discipline = map.get("discipline");//学科 1软件 2网络 3通用
        Object chapterId = map.get("chapterId"); //章节id
        Object sourced = map.get("sourced"); //试题来源 1课程试题 2面试宝典 3企业真题
        Object companyId = map.get("companyId"); //企业id
        Object status = map.get("status"); //启禁状态 1启用 0禁用


        switch (type){

            case "1" :

                QueryWrapper<Single> queryWrapper = new QueryWrapper<>();

                Object singleContent = map.get("singleContent");

                if(singleContent != null) queryWrapper.like("single_content",singleContent.toString());


                if(discipline != null) queryWrapper.eq("discipline",discipline.toString());

                if(chapterId != null) queryWrapper.eq("chapter_id",chapterId.toString());

                if(sourced != null) queryWrapper.eq("sourced",sourced.toString());

                if(companyId != null) queryWrapper.eq("company_id",companyId.toString());

                if(status != null) queryWrapper.eq("status",status.toString());


                queryWrapper.eq("status","1");

                queryWrapper.orderByDesc("create_time");

                IPage<Single> singleIPage = new Page<>(page,size);

                IPage<Single> result = singleMapper.selectPage(singleIPage, queryWrapper);

                PageResult<Single> pageResult = new PageResult<>(result.getTotal(),result.getRecords());

                return new Result(ResultCode.SUCCESS,pageResult);

            case "2":

                QueryWrapper<Selection> selectionQueryWrapper = new QueryWrapper<>();

                Object selectionContent = map.get("selectionContent");

                if(selectionContent != null) selectionQueryWrapper.like("selection_content",selectionContent.toString());



                if(discipline != null) selectionQueryWrapper.eq("discipline",discipline.toString());

                if(chapterId != null) selectionQueryWrapper.eq("chapter_id",chapterId.toString());

                if(sourced != null) selectionQueryWrapper.eq("sourced",sourced.toString());

                if(companyId != null) selectionQueryWrapper.eq("company_id",companyId.toString());

                if(status != null) selectionQueryWrapper.eq("status",status.toString());

                selectionQueryWrapper.eq("status","1");

                selectionQueryWrapper.orderByDesc("create_time");

                IPage<Selection> selectionIPage = new Page<>(page,size);

                IPage<Selection> selectionResult = selectionMapper.selectPage(selectionIPage, selectionQueryWrapper);

                PageResult<Selection> selectionPageResult = new PageResult<>(selectionResult.getTotal(),selectionResult.getRecords());

                return new Result(ResultCode.SUCCESS,selectionPageResult);

            case "3":

                QueryWrapper<Ask> askQueryWrapper = new QueryWrapper<>();

                Object askContent = map.get("askContent");

                if(askContent != null) askQueryWrapper.like("ask_content",askContent.toString());


                if(discipline != null) askQueryWrapper.eq("discipline",discipline.toString());

                if(chapterId != null) askQueryWrapper.eq("chapter_id",chapterId.toString());

                if(sourced != null) askQueryWrapper.eq("sourced",sourced.toString());

                if(companyId != null) askQueryWrapper.eq("company_id",companyId.toString());

                if(status != null) askQueryWrapper.eq("status",status.toString());

                askQueryWrapper.eq("status","1");

                askQueryWrapper.orderByDesc("create_time");

                IPage<Ask> askIPage = new Page<>(page,size);

                IPage<Ask> askIPageResult = askMapper.selectPage(askIPage, askQueryWrapper);

                PageResult<Ask> askPageResult = new PageResult<>(askIPageResult.getTotal(),askIPageResult.getRecords());

                return new Result(ResultCode.SUCCESS,askPageResult);

            case "4":

                QueryWrapper<Upper> upperQueryWrapper = new QueryWrapper<>();

                Object upperContent = map.get("upperContent");

                if(upperContent != null) upperQueryWrapper.like("upper_content",upperContent.toString());


                if(discipline != null) upperQueryWrapper.eq("discipline",discipline.toString());

                if(chapterId != null) upperQueryWrapper.eq("chapter_id",chapterId.toString());

                if(sourced != null) upperQueryWrapper.eq("sourced",sourced.toString());

                if(companyId != null) upperQueryWrapper.eq("company_id",companyId.toString());

                if(status != null) upperQueryWrapper.eq("status",status.toString());


                upperQueryWrapper.eq("status","1");

                upperQueryWrapper.orderByDesc("create_time");

                IPage<Upper> upperIPage = new Page<>(page,size);

                IPage<Upper> upperIPageResult = upperMapper.selectPage(upperIPage, upperQueryWrapper);

                PageResult<Upper> upperPageResult = new PageResult<>(upperIPageResult.getTotal(),upperIPageResult.getRecords());

                return new Result(ResultCode.SUCCESS,upperPageResult);

            default:
                return Result.FAIL();
        }
    }

    public Result save(Map<String, Object> map) throws Exception {

        String type = map.get("type").toString();

        String chapterId = map.get("chapterId").toString();

        Chapter chapter = chapterMapper.selectById(chapterId);

        String sourced = map.get("sourced").toString();

        String companyName = null;

        if("3".equals(sourced)){
            String companyId = map.get("companyId").toString();
            Company company = companyMapper.selectById(companyId);
            companyName = company.getName();
        } else {
            map.put("companyId",null);
        }

        switch (type){
            case "1":

                Single single = BeanMapUtils.mapToBean(map, Single.class);
                single.setChapterName(chapter.getName());
                single.setCompanyName(companyName);
                single.setCreateTime(new Date());
                single.setId(idWorker.nextId() + "");
                single.setStatus("1");
                single.setModifyId(currentUser().getId());
                single.setModifyUser(currentUser().getNickName());
                single.setModifyTime(new Date());
                single.setEnVisible("1");
                singleMapper.insert(single);

                return Result.SUCCESS();
            case "2":

                Selection selection = BeanMapUtils.mapToBean(map, Selection.class);
                selection.setChapterName(chapter.getName());
                selection.setCompanyName(companyName);
                selection.setCreateTime(new Date());
                selection.setId(idWorker.nextId() + "");
                selection.setStatus("1");
                selection.setModifyId(currentUser().getId());
                selection.setModifyUser(currentUser().getNickName());
                selection.setModifyTime(new Date());
                selection.setEnVisible("1");
                selectionMapper.insert(selection);
                return Result.SUCCESS();
            case "3":

                Ask ask = BeanMapUtils.mapToBean(map, Ask.class);
                ask.setChapterName(chapter.getName());
                ask.setCompanyName(companyName);
                ask.setCreateTime(new Date());
                ask.setId(idWorker.nextId() + "");
                ask.setStatus("1");
                ask.setModifyId(currentUser().getId());
                ask.setModifyUser(currentUser().getNickName());
                ask.setModifyTime(new Date());
                ask.setEnVisible("1");
                askMapper.insert(ask);
                return Result.SUCCESS();
            case "4":
                //因为新增上机题的功能在上传文件中实现, 所以这里一百年也不会触发
                return Result.FAIL();
            default:
                return Result.FAIL();
        }


    }

    public Result update(Map<String, Object> map) throws Exception {
        String type = map.get("type").toString(); //强制条件
        String id = map.get("id").toString(); //强制条件

        if(map.get("createTime") != null){
            String createTime = map.get("createTime").toString(); //把前端传递的long类型的createTime转换成Date
            long time = Long.parseLong(createTime);

            Date date = new Date(time);
            map.put("createTime",date);
        }


        if(map.get("modifyTime") != null){
            String modifyTime = map.get("modifyTime").toString(); //把前端传递的long类型的createTime转换成Date
            long time = Long.parseLong(modifyTime);

            Date date = new Date(time);
            map.put("modifyTime",date);
        }
        switch (type){
            case "1":
                Single singleTarget = singleMapper.selectById(id);

                Single single = BeanMapUtils.mapToBean(map, Single.class);

                if(!"3".equals(single.getSourced())){
                    single.setCompanyName(null);
                    single.setCompanyId(null);
                    singleTarget.setCompanyName(null);
                    singleTarget.setCompanyId(null);
                }

                if(single.getCompanyId() != null){
                    if(!single.getCompanyId().equals(singleTarget.getCompanyId())){
                        Company company = companyMapper.selectById(single.getCompanyId());
                        single.setCompanyName(company.getName());
                    }
                }

                if(!single.getChapterId().equals(singleTarget.getChapterId())){
                    Chapter chapter = chapterMapper.selectById(single.getChapterId());
                    single.setChapterName(chapter.getName());
                }

                BeanUtils.copyProperties(single,singleTarget);

                singleTarget.setModifyId(currentUser().getId());
                singleTarget.setModifyUser(currentUser().getNickName());
                singleTarget.setModifyTime(new Date());

                singleMapper.updateById(singleTarget);

                return Result.SUCCESS();
            case "2":

                Selection selectionTarget = selectionMapper.selectById(id);

                Selection selection = BeanMapUtils.mapToBean(map, Selection.class);

                if(!"3".equals(selection.getSourced())){
                    selection.setCompanyName(null);
                    selection.setCompanyId(null);
                    selectionTarget.setCompanyName(null);
                    selectionTarget.setCompanyId(null);
                }

                if(selection.getCompanyId() != null){
                    if(!selection.getCompanyId().equals(selectionTarget.getCompanyId())){
                        Company company = companyMapper.selectById(selection.getCompanyId());
                        selection.setCompanyName(company.getName());
                    }
                }

                if(!selection.getChapterId().equals(selectionTarget.getChapterId())){
                    Chapter chapter = chapterMapper.selectById(selection.getChapterId());
                    selection.setChapterName(chapter.getName());
                }

                BeanUtils.copyProperties(selection,selectionTarget);

                selectionTarget.setModifyId(currentUser().getId());
                selectionTarget.setModifyUser(currentUser().getNickName());
                selectionTarget.setModifyTime(new Date());

                selectionMapper.updateById(selectionTarget);

                return Result.SUCCESS();
            case "3":

                Ask askTarget = askMapper.selectById(id);

                Ask ask = BeanMapUtils.mapToBean(map, Ask.class);

                if(!"3".equals(ask.getSourced())){
                    ask.setCompanyName(null);
                    ask.setCompanyId(null);
                    askTarget.setCompanyName(null);
                    askTarget.setCompanyId(null);
                }

                if(ask.getCompanyId() != null){
                    if(!ask.getCompanyId().equals(askTarget.getCompanyId())){
                        Company company = companyMapper.selectById(ask.getCompanyId());
                        ask.setCompanyName(company.getName());
                    }
                }

                if(!ask.getChapterId().equals(askTarget.getChapterId())){
                    Chapter chapter = chapterMapper.selectById(ask.getChapterId());
                    ask.setChapterName(chapter.getName());
                }

                BeanUtils.copyProperties(ask,askTarget);

                askTarget.setModifyId(currentUser().getId());
                askTarget.setModifyUser(currentUser().getNickName());
                askTarget.setModifyTime(new Date());

                askMapper.updateById(askTarget);

                return Result.SUCCESS();
            case "4":

                Upper upperTarget = upperMapper.selectById(id);

                Upper upper = BeanMapUtils.mapToBean(map, Upper.class);


                if(!"3".equals(upper.getSourced())){
                    upper.setCompanyName(null);
                    upper.setCompanyId(null);
                    upperTarget.setCompanyName(null);
                    upperTarget.setCompanyId(null);
                }

                if(upper.getCompanyId() != null){
                    if(!upper.getCompanyId().equals(upperTarget.getCompanyId())){
                        Company company = companyMapper.selectById(upper.getCompanyId());
                        upper.setCompanyName(company.getName());
                    }
                }

                if(!upper.getChapterId().equals(upperTarget.getChapterId())){
                    Chapter chapter = chapterMapper.selectById(upper.getChapterId());
                    upper.setChapterName(chapter.getName());
                }

                BeanUtils.copyProperties(upper,upperTarget);

                upperTarget.setStatus("1");


                upperTarget.setCreateTime(new Date());

                upperTarget.setModifyId(currentUser().getId());
                upperTarget.setModifyTime(new Date());
                upperTarget.setModifyUser(currentUser().getNickName());
                upperTarget.setEnVisible("1");
                upperMapper.updateById(upperTarget);

                return Result.SUCCESS();
            default:
                return Result.FAIL();
        }
    }

    @Value("${ftp.host}")
    private String FTP_HOST;
    @Value("${ftp.port}")
    private String FTP_PORT;
    @Value("${ftp.username}")
    private String FTP_USERNAME;
    @Value("${ftp.password}")
    private String FTP_PASSWORD;

    public Result upload(String originalFilename, InputStream inputStream) {

        String newFileName = idWorker.nextId() + originalFilename.substring(originalFilename.lastIndexOf("."));

        SftpUtil sftpUtil = new SftpUtil(FTP_HOST, Integer.parseInt(FTP_PORT), FTP_USERNAME, FTP_PASSWORD);
        sftpUtil.upload("/var/ftp/pub",newFileName,inputStream);

        String url = "ftp://".concat(FTP_HOST).concat(":21/pub/").concat(newFileName);

        Upper upper = Upper.builder()
                .id(idWorker.nextId() + "")
                .upperUrl(url)
                .createTime(new Date())
                .build();

        upper.setModifyId(currentUser().getId());
        upper.setModifyUser(currentUser().getNickName());
        upper.setModifyTime(new Date());

        upperMapper.insert(upper);

        return new Result(ResultCode.SUCCESS,upper);
    }

    public Result getQuestionsByChapterIdsAndQuestionTypeIds(Map<String, Object> map) {

        String chapterIds = map.get("chapterIds").toString();
        String questionTypeIds = map.get("questionTypeIds").toString();

        List<Single> singleList = new ArrayList<>();
        List<Selection> selectionList = new ArrayList<>();
        List<Ask> askList = new ArrayList<>();


        for (String chapterId : chapterIds.split(",")) {
            if(questionTypeIds.contains("1")){ //单选题
                QueryWrapper<Single> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("chapter_id",chapterId);
                queryWrapper.eq("type","1");
                List<Single> singles = singleMapper.selectList(queryWrapper);
                if(singles != null && singles.size() > 0){
                    for (Single single : singles) {
                        singleList.add(single);
                    }
                }
            }
            if(questionTypeIds.contains("2")){ //多选题
                QueryWrapper<Selection> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("chapter_id",chapterId);
                queryWrapper.eq("type","2");
                List<Selection> selections = selectionMapper.selectList(queryWrapper);
                if(selections != null && selections.size() > 0){
                    for (Selection selection : selections) {
                        selectionList.add(selection);
                    }
                }
            }
            if(questionTypeIds.contains("3")){ //问答题
                QueryWrapper<Ask> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("chapter_id",chapterId);
                queryWrapper.eq("type","3");
                List<Ask> asks = askMapper.selectList(queryWrapper);
                if(asks != null && asks.size() > 0){
                    for (Ask ask : asks) {
                        askList.add(ask);
                    }
                }
            }
        }

        QuestionTypeVo vo = new QuestionTypeVo();
        vo.setSingleList(singleList);
        vo.setSelectionList(selectionList);
        vo.setAskList(askList);

        return new Result(ResultCode.SUCCESS,vo);

    }
    

    public List<Single> selectList() {
        return singleMapper.selectList(null);
    }

    public String exportData(Map<String, Object> map, HttpServletResponse response) {

        List<String> list = (List<String>) map.get("excelOptions");

        //排序有序表头
        List<String> tableList = new ArrayList<>();

        //有序对象属性名
        List<String> colNameList = new ArrayList<>();

        if(list.contains("discipline")){ tableList.add("学科");colNameList.add("discipline"); }
        if(list.contains("chapterName")){ tableList.add("章节名称");colNameList.add("chapterName");}
        if(list.contains("sourced")){ tableList.add("来源");colNameList.add("sourced"); }
        if(list.contains("companyName")){ tableList.add("企业名称");colNameList.add("companyName"); }
        if(list.contains("singleContent")){ tableList.add("题干");colNameList.add("singleContent"); }
        if(list.contains("singleOptionA")){ tableList.add("选项A");colNameList.add("singleOptionA"); }
        if(list.contains("singleOptionB")){ tableList.add("选项B");colNameList.add("singleOptionB"); }
        if(list.contains("singleOptionC")){ tableList.add("选项C");colNameList.add("singleOptionC"); }
        if(list.contains("singleOptionD")){ tableList.add("选项D");colNameList.add("singleOptionD"); }
        if(list.contains("singleAsk")){ tableList.add("答案");colNameList.add("singleAsk"); }
        if(list.contains("status")){ tableList.add("状态");colNameList.add("status"); }

        //将列集合转换为数组
        String [] columnNames = colNameList.toArray(new String [colNameList.size()]);

        String [] tableHeader = tableList.toArray(new String [tableList.size()]);

        //查询数据
        List<Single> singles = singleMapper.selectList(null);

        boolean showOrHiddenHeader = "2".equals(map.get("showOrHiddenHeader").toString());

        PoiUtil.downLoadExcel(response,columnNames,tableHeader,singles,"数据.xlsx",showOrHiddenHeader,showOrHiddenHeader ? map.get("headerContent").toString():"");

        return null;
    }

    public Result uploadQuestion(MultipartFile file) throws IOException {
        List<String[]> data = PoiUtil.readExcel(file);

        for (String[] row : data) {

            QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
            chapterQueryWrapper.eq("name",row[1]);

            Chapter chapter = chapterMapper.selectOne(chapterQueryWrapper);

            QueryWrapper<Company> companyQueryWrapper = new QueryWrapper<>();
            companyQueryWrapper.eq("name",row[3]);

            Company company = companyMapper.selectOne(companyQueryWrapper);

            Single single = Single.builder()
                    .discipline(row[0])
                    .chapterName(row[1])
                    .chapterId(chapter == null ? null : chapter.getId())
                    .sourced(row[2])
                    .companyName(row[3])
                    .companyId(company == null ? null : company.getId())
                    .singleContent(row[4])
                    .singleOptionA(row[5])
                    .singleOptionB(row[6])
                    .singleOptionC(row[7])
                    .singleOptionD(row[8])
                    .singleAsk(row[9])
                    .status(row[10])
                    .createTime(new Date())
                    .type("1")
                    .id(idWorker.nextId() + "")
                    .build();

            single.setModifyId(currentUser().getId());
            single.setModifyUser(currentUser().getNickName());
            single.setModifyTime(new Date());


            singleMapper.insert(single);
        }
        return Result.SUCCESS();
    }

}
