package com.XYF.crowd.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @username 熊一飞
 */


//自定义配置String类型对象型放入到yml文件中
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component//将此类给spring容器进行管理
@ConfigurationProperties(value = "short.message")
public class ShortMessageProperties {

    private String customizeHost;
    private String customizePath;
    private String customizeMethod;
    private String appCode;
    private String templateId;
    private String sign;

}
