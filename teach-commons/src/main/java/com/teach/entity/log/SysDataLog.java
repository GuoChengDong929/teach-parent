package com.teach.entity.log;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @Author 韩雪松
 * @Date 2019/12/25
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysDataLog implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String dataId;  //操作的记录id

    private String operation; //操作类型 insert update delete

    private String operationId; //操作人id

    private String operationUser; //操作人姓名

    private Date operationTime; //操作时间

    private String operationParams; //请求参数

    private String type; //触发名称

}
