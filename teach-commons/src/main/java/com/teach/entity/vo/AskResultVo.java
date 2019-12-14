package com.teach.entity.vo;

import com.teach.entity.academic.question.Ask;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author 韩雪松
 * @Date 2019/12/12
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AskResultVo  implements Serializable {

    private List<Ask> asks;

    private List<AskVo> list;
}
