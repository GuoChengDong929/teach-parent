package com.teach.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author 韩雪松
 * @Date 2019/12/22
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentScoreVo implements Serializable {

    private List<String> headers;

    private List<StudentVo> vos;

}
