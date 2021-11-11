package com.XYF.crowd.mvc.config;

/**
 * @username 熊一飞
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

//security中文提示信息配置类
@Configuration
public class ReloadMessageConfig {
    @Bean //加载中文认证提示信息
    public ReloadableResourceBundleMessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        //加载org/springframework/security包下的中文提示信息 配置文件
        messageSource.setBasename("classpath:org/springframework/security/messages_zh_CN");
        return messageSource;
    }
}
