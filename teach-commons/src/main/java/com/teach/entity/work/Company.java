package com.teach.entity.work;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@TableName("wr_company")
public class Company implements Serializable {

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    private String name;
}
