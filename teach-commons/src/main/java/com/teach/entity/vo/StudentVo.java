package com.teach.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author 韩雪松
 * @Date 2019/12/22
 **/
@Data
public class StudentVo implements Serializable {

    private String studentId;
    private String nickName;
    private List<Integer>scores;
}
