package com.teach.entity.academic.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * @Description:
 * @Author 韩雪松
 * @Date 2019/12/13
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@TableName("st_selection_result")
public class SelectionResult implements Serializable {

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    private String optionIds;
    private String selectionId;
    private String examId;
    private String studentId;
}
