package com.teach.entity.system.dictionaries;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("sys_dictionaries")
public class Dictionaries implements Serializable {

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    private String name;

    private String code;

    private String useScenarios;

    private String description;

    private Date createTime;
}
