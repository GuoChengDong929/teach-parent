package com.teach.aop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author 韩雪松
 * @Date 2019/12/25
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "operation")
public class OperationProperties {

    //开启异步记录用户操作日志
    private boolean openAopLog = true;
}
