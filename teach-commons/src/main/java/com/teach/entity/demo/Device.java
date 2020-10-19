package com.teach.entity.demo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("device")
public class Device implements Serializable {

    @TableId(type = IdType.ID_WORKER_STR)
    private Integer id;

    @TableField("device_name")
    private String deviceName; //设备名称

    @TableField("device_type")
    private String deviceType; //设备型号

    @TableField("device_num")
    private Integer deviceNum; //设备数量

    @TableField("device_price")
    private String devicePrice; //设备价格（万元）

    @TableField("device_total")
    private String deviceTotal; //设备总额

    @TableField("supplier")
    private String supplier; //供应方
}
