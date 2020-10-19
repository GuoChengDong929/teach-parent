package com.teach.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 成绩监测  日/周/月考的成绩图表统计
 * @Author 韩雪松
 * @Date 2019/12/30
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComparisonVo implements Serializable {

    private List<Date> dates;
    private List<Double> scores;
}
