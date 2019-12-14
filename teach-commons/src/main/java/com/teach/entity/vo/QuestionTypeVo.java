package com.teach.entity.vo;

import com.teach.entity.academic.question.Ask;
import com.teach.entity.academic.question.Selection;
import com.teach.entity.academic.question.Single;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QuestionTypeVo implements Serializable {

    List<Single> singleList;
    List<Selection> selectionList;
    List<Ask> askList;
}
