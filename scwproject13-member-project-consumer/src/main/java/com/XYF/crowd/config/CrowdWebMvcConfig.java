package com.XYF.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @username 熊一飞
 */


@Component
public class CrowdWebMvcConfig implements WebMvcConfigurer {


    public void addViewControllers(ViewControllerRegistry registry) {

//        由于view-controller是在project-consumer内部定义的，所以这是一个不经过zuul访问的地址
        registry.addViewController("/agree/project/page.html").setViewName("project-agree");
        registry.addViewController("/launch/project/page.html").setViewName("project-launch");
        registry.addViewController("/return/info/page.html").setViewName("project-return");
        registry.addViewController("/confirm/project/page.html").setViewName("project-confirm");
        registry.addViewController("/success/project/page.html").setViewName("project-success");

    }
}
