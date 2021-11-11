package com.XYF.crowd.mvc.config;

import com.XYF.crowd.constant.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @username 熊一飞
 */

//将此类作为security的配置类
@Configuration
//表示启用 spring security安全框架的功能
@EnableWebSecurity
@SuppressWarnings("all")
/*
启用方法级别的认证
        prePostEnabled boolean类型，默认为false
        true ：表示可以启用@PreAuthorize注解和 @PostAuthorize注解
 */
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    //注入UserDetails类
    @Autowired
    private CrowdUserDetailsService crowdUserDetailsService;

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

//        使用基于数据库的认证
        auth.userDetailsService(crowdUserDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        与springSecurity环境下请求授权相关

        /*

        authorizeRequests() 对请求进行授权
        antMatchers("/admin/do/login.html") 针对登录页进行设置
        permitAll() 无条件访问
        antMatchers() 针对静态资源进行设置，参数可以有多个
        formLogin()开启表单登录的功能
        loginPage()  指定登录页面
        loginProcessingUrl() 指定处理登录请求的地址
        defaultSuccessUrl()  指定登录成功后前往的页面
        usernameParameter()   账号的请求参数名称，即表单账号input的name属性
        passwordParameter()   密码的请求参数名称，即表单密码input的name属性
        csrf.disable() 禁用跨站攻击保护
        logout()    开启退出登录功能
        and()  起连接作用
        logoutUrl()    指定退出登录地址，就是点击退出按钮的那个地址
        logoutSuccessUrl()   指定退出后前往的页面

  */


//        设定没有权限访问后进入的页面,先舍弃，因为无法应用到注解上
//        不是注解形式不执行异常反射机制：DelegatingFilterProxy后直接检测是否有权限，没有就直接到了403界面，不执行DispatchServlet
//        http.exceptionHandling().accessDeniedPage("handler请求"); 或者以下
        http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                request.setAttribute("exception", new Exception(CrowdConstant.MESSAGE_NOT_AUTHORITY));
                request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request,response);
            }
        });
//
//        权限注解形式：先执行DelegatingFilterProxy，再去执行DispatchServlet中，然后才会执行springmvc拦截器再到检测到有权限注解，然后权限不足就执行异常映射机制


        http.authorizeRequests().antMatchers("/admin/do/login.html").permitAll()
                .antMatchers("/login/page.html").permitAll()
                .antMatchers("/bootstrap/**", "/css/**", "/fonts/**", "/img/**", "/jquery/**", " /layer/**", "/script/**", "/ztree/**")
                .permitAll()
                .antMatchers("/admin/get/page.html")//针对分页显示admin数据设定访问控制
//                .hasRole("经理")  //要求具备经理的角色
                .access("hasRole('经理')or hasAuthority('user:get')")  //要求要么有经理角色，要么就有user:get权限
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login/page.html")
                .loginProcessingUrl("/security/do/login.html")
                .defaultSuccessUrl("/main/page.html",true)
                .usernameParameter("loginAcct")
                .passwordParameter("userPswd")
                .and()
                .logout()
                .logoutUrl("/security/do/logout.html")
                .logoutSuccessUrl("/admin/do/login.html")
                .and()
                .csrf().disable();

    }
}
