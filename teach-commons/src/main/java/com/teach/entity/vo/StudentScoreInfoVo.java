package com.teach.entity.vo;

import com.teach.entity.academic.exam.Score;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @Author 韩雪松
 * @Date 2019/12/23
 **/
@Data
public class StudentScoreInfoVo implements Serializable {

    private Date examTime;
    private Integer score;
}
