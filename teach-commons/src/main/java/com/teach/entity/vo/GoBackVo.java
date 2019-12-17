package com.teach.entity.vo;


import com.teach.entity.academic.exam.AskResult;
import com.teach.entity.academic.exam.Score;
import com.teach.entity.academic.exam.SelectionResult;
import com.teach.entity.academic.exam.SingleResult;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class GoBackVo implements Serializable {


    private Score score;
    private List<AskResult> askResults;
    private List<SingleResult> singleResults;
    private List<SelectionResult> selectionResults;

}
