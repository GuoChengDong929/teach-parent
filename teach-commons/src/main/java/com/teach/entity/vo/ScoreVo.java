package com.teach.entity.vo;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ScoreVo implements Serializable {

    private String studentId;

    private String examName;

    private Integer singleSucc;

    private Integer singleErr;

    private String singleSuccIds;

    private String singleErrIds;

    private Integer selectionSucc;

    private Integer selectionErr;

    private String selectionSuccIds;

    private String selectionErrIds;

    private Integer selectionScore;

    private Integer singleScore;

    private Integer score;

    private String status;

    private String nickName;

}
