package com.XYF.crowd.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @username 熊一飞
 */

/*
implements WebMvcConfigurer 相当于在配置文件中配置了一个view-controller
 */

@Component //需要加入这个注解将这个类作为bean注入到服务中
public class CrowdWebMvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {

        // 浏览器访问的地址,注册页面的地址
        String urlPath = "/auth/member/to/reg/page.html";

//        目标视图的名称,将来拼接“prefix；classpath:/templates/","suffix.html等后缀
        String viewName = "member-reg";

//        添加view-controller
        registry.addViewController(urlPath).setViewName(viewName);
        registry.addViewController("/auth/member/to/login/page.html").setViewName("member-login");
        registry.addViewController("/auth/member/to/center/page.html").setViewName("member-center");
//        registry.addViewController("/member/my/crowd/page.html").setViewName("member-crowd");



    }
}
