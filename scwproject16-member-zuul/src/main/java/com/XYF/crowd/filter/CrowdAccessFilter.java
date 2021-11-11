package com.XYF.crowd.filter;

import com.XYF.crowd.constant.AccessPassResource;
import com.XYF.crowd.constant.CrowdConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @username 熊一飞
 */

/*



实现两个：
1、是当前请求,返回false后直接放行
2、不是当前请求，但是可能是静态资源过或者非静态资源，那么再进行判定返回true还是false

 */

@Component
public class CrowdAccessFilter extends ZuulFilter {

    public String filterType() {
//        代表微服务执行前进行过滤
        return "pre";
    }

    public int filterOrder() {
        return 0;
    }

    /*
      过滤：返回true，继续执行run()方法
     不过滤：返回false，直接放行

     */
    public boolean shouldFilter() {


//        1、获取RequestContext对象
        RequestContext requestContext = RequestContext.getCurrentContext();

//        2、获取request对象
        HttpServletRequest request = requestContext.getRequest();


        String servletPath = request.getServletPath();


//        判断是否是当前请求
        boolean isCurrentPath = AccessPassResource.PASS_RES_SET.contains(servletPath);
        if (isCurrentPath || servletPath.equals("/")) {
            return false;
        }



/*
  判断是否是静态资源
   工具类返回true：本方法返回false，跳过登录检查
   工具类返回false：本方法返回true，需要进行登录检查

 */
        return !AccessPassResource.judgeCurrentServletPathWhetherStaticResource(servletPath);
    }

    //    执行登录检查
    public Object run() throws ZuulException {

        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        HttpServletResponse response = context.getResponse();
        HttpSession session = request.getSession();

//        1.尝试取出session中保存的用户
        Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_MEMBER);

//        2.判断该用户是否有，没有说明session过期，说明登录失效了
        if (loginMember == null) {

//         3.将提示消息存入 Session 域

            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);
//         4.那么返回登录页面

            try {
                response.sendRedirect("/auth/member/to/login/page.html");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


//
        return null;
    }
}
