package com.XYF.crowd.constant;

import org.springframework.security.access.method.P;

import java.util.HashSet;
import java.util.Set;

/**
 * @username 熊一飞
 */

/*
保存放行的地址
 */
public class AccessPassResource {

    public static final Set<String> PASS_RES_SET = new HashSet<>();

//    初始化HashSet
    static {
        PASS_RES_SET.add("/auth/member/to/reg/page.html");
        PASS_RES_SET.add("/auth/member/to/login/page.html");
        PASS_RES_SET.add("/auth/member/do/login");
        PASS_RES_SET.add("/auth/do/member/register");
        PASS_RES_SET.add("/auth/member/send/short/message.json");
        PASS_RES_SET.add("/auth/member/to/logout");
    }

    public static final Set<String> STATIC_RES_SET = new HashSet<String>();

    static {
        STATIC_RES_SET.add("bootstrap");
        STATIC_RES_SET.add("fonts");
        STATIC_RES_SET.add("img");
        STATIC_RES_SET.add("jquery");
        STATIC_RES_SET.add("layer");
        STATIC_RES_SET.add("script");
        STATIC_RES_SET.add("ztree");
        STATIC_RES_SET.add("css");
    }

    /**
     * 用于判断某个 ServletPath 值是否对应一个静态资源
     * @param servletPath
     * @return
     * true：是静态资源
     * false：不是静态资源
     */
    public static boolean judgeCurrentServletPathWhetherStaticResource(String servletPath) {

        if (servletPath.length() ==0 || servletPath==null){
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }

//        通过斜杆来切割
        String[] splits = servletPath.split("/");

//        切割后的元素获取，切割后位[" ","aaa","bbb"]我们需要的为第2个元素
        String path = splits[1];

        return STATIC_RES_SET.contains(path);
    }


}
