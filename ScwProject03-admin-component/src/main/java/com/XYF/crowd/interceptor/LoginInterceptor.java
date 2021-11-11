package com.XYF.crowd.interceptor;



import com.XYF.crowd.constant.CrowdConstant;
import com.XYF.crowd.entity.Admin;
import com.XYF.crowd.exception.IllegalLoginException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @username 熊一飞
 */
public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        //获取请求路径
        String path = request.getRequestURI();
        //获取session域
        Admin admin = (Admin) request.getSession().getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);
      /*  if ("login/page.html".equals(path)){
            return true;
        }else {
            if (admin!=null){
                return true;
            }else {
                throw new IllegalLoginException(CrowdConstant.MESSAGE_ILLEGAL_LOGIN);
            }
        }*/

      if (admin==null){
          throw new IllegalLoginException(CrowdConstant.MESSAGE_ILLEGAL_LOGIN);
      }
      return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
