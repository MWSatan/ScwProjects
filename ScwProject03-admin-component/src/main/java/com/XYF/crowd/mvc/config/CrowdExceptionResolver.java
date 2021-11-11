package com.XYF.crowd.mvc.config;

import com.XYF.crowd.constant.CrowdConstant;
import com.XYF.crowd.exception.*;
import com.XYF.crowd.util.CrowdUtil;
import com.XYF.crowd.util.ResultEntity;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @username 熊一飞
 */

/*
注意：
  一定不能使用@Controller这个注解，否则会先跳到xml文件的注册视图中
  而@ControllerAdvice的使用是先执行本类中的视图
 */
@ControllerAdvice
public class CrowdExceptionResolver {


    private Logger logger = LoggerFactory.getLogger(CrowdExceptionResolver.class);

    //    AccessDeniedException为springSecurity权限访问抛出的异常
    @ExceptionHandler(value = AccessDeniedException.class)
    public ModelAndView AccessDeniedException(AccessDeniedException exception//            当前请求对象
            , HttpServletRequest request
//            响应对象
            , HttpServletResponse response
    ) throws IOException {

//        指定视图名称
        String view = "error";

        return commonResolver(view, request, response, exception);
    }

    @ExceptionHandler(value = UpdateLoginAcctAlreadyInUseException.class)
    public ModelAndView UpdateLoginAcctAlreadyInUseException(UpdateLoginAcctAlreadyInUseException exception//            当前请求对象
            , HttpServletRequest request
//            响应对象
            , HttpServletResponse response
    ) throws IOException {

//        指定视图名称
        String view = "error";

        return commonResolver(view, request, response, exception);
    }

    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView LoginAcctAlreadyInUseException(LoginAcctAlreadyInUseException exception//            当前请求对象
            , HttpServletRequest request
//            响应对象
            , HttpServletResponse response
    ) throws IOException {

//        指定视图名称
        String view = "admin-add";
        return commonResolver(view, request, response, exception);
    }

    @ExceptionHandler(value = IllegalLoginException.class)
    public ModelAndView resolveIllegalLoginException(IllegalLoginException exception//            当前请求对象
            , HttpServletRequest request
//            响应对象
            , HttpServletResponse response
    ) throws IOException {

//        指定视图名称
        String view = "admin-login";
        return commonResolver(view, request, response, exception);
    }

    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginFailException(LoginFailedException exception//            当前请求对象
            , HttpServletRequest request
//            响应对象
            , HttpServletResponse response
    ) throws IOException {


        logger.info("执行LoginFailedException对象");

//        指定视图名称
        String view = "admin-login";
        return commonResolver(view, request, response, exception);

    }

    @ExceptionHandler(value = ArithmeticException.class)
    public ModelAndView resolveMathException(ArithmeticException exception//            当前请求对象
            , HttpServletRequest request
//            响应对象
            , HttpServletResponse response
    ) throws IOException {

//        指定视图名称
        String view = "error";
        return commonResolver(view, request, response, exception);

    }


    //    ExceptionHandler讲一个具体的异常类型和一个方法关联起来
    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolverExceptionNullPointerException(
//            实际捕获到的异常类型
            NullPointerException exception
//            当前请求对象
            , HttpServletRequest request

//            响应对象
            , HttpServletResponse response
    ) throws IOException {


//        指定视图名称
        String viewName = "error";
        return commonResolver(viewName, request, response, exception);
    }


    /**
     * 公共方法
     *
     * @param view      视图
     * @param request   请求
     * @param response  响应
     * @param exception 异常类
     * @return
     * @throws IOException
     */
    private ModelAndView commonResolver(String view, HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {

//        判断当前请求类型
        boolean flag = CrowdUtil.judgeRequestType(request);
//        如果是ajax请求
        if (flag) {
//            创建一个ResultEntity对象

//            exception.getMessage() 获取异常信息
            ResultEntity<Object> resultEntity = ResultEntity.failWithMessage(exception.getMessage());

//            创建Gson对象，Gson可以对json对象序列化以及反序列化
            Gson gson = new Gson();

//            将ResultEntity对象转为Json对象（反序列化）
            String json = gson.toJson(resultEntity);

//            将json对象反应给浏览器
            response.getWriter().write(json);

//            由于上面已经通过原生的response对象返回了响应，所以不提供ModelAndView对象
            return null;
        } else {

//        如果不是ajax请求则创建ModelAndView对象
            ModelAndView model = new ModelAndView();

//        将exception对象存入模型中
            model.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);

//        设置对应的视图名称
            model.setViewName(view);

            return model;
        }

    }

}



