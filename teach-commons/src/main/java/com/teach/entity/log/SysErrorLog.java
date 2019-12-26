package com.teach.entity.log;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_error_log")
public class SysErrorLog implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;



    private String operation;//方法描述


    private String method;//执行方法

    private String errorType;//异常类型


    private String message;//异常消息


    private String params;//方法参数


    private String ip;//远程访问主机ip

    private Date createTime;//执行时间

    private String modifyUser;
    private String modifyId;

}