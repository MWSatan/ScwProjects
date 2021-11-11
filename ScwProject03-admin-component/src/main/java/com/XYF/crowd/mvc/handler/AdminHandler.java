package com.XYF.crowd.mvc.handler;

import com.XYF.crowd.constant.CrowdConstant;
import com.XYF.crowd.entity.Admin;
import com.XYF.crowd.exception.LoginFailedException;
import com.XYF.crowd.mvc.config.CrowdUserDetailsService;
import com.XYF.crowd.mvc.config.SecurityAdmin;
import com.XYF.crowd.service.AdminService;
import com.XYF.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.taglibs.authz.AuthenticationTag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

/**
 * @username 熊一飞
 */

@Controller
@SuppressWarnings("all")
public class AdminHandler {

    @Autowired
    private AdminService adminService;

    private Logger logger = LoggerFactory.getLogger(AdminHandler.class);


    @RequestMapping(value = "/admin/edit.html")
    public String editAdmin(
            Admin admin,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("keyword") String keyword,
            ModelMap modelMap, HttpServletRequest request
    ) {
        adminService.editAdmin(admin);
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    @RequestMapping(value = "/admin/to/edit/page.html")
    public String toEditAdmin(
            @RequestParam(value = "adminId") Integer adminId,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("keyword") String keyword,
            ModelMap modelMap
    ) {

        Admin admin = adminService.getAdminById(adminId);

        modelMap.addAttribute("admin", admin);
        return "admin-edit";
    }

    @RequestMapping(value = "/admin/save.html")
    @PreAuthorize("hasAuthority('user:save')")
    public String addAdmin(Admin admin) {

        adminService.saveAdmin(admin);
//为了定义到最后一页，将页码定义一个最大的数 Integer.MAX_VALUE
        return "redirect:/admin/get/page.html?pageNum=" + Integer.MAX_VALUE;
    }

    /**
     * @param id 删除所需要的id
     * @PathVariable @PathVariable是spring3.0的一个新功能：接收请求路径中占位符的值
     */
    @RequestMapping("/admin/remove/{adminId}.json")
    @ResponseBody
    public boolean reSearch(@PathVariable(value = "adminId") Integer id,
                            HttpSession session, ModelAndView model
    ) throws LoginFailedException {


//        找到当前的用户,由于现在的用户登录是用security做的，所以没有了session，所以得使用其它办法来找
        //在CrowdUserDetailsService获取，定义了一个属性来接收这个Admin对象
//        Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);


        SecurityContext context_session = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication = context_session.getAuthentication();

        SecurityAdmin securityAdmin = (SecurityAdmin) authentication.getPrincipal();

        Admin admin = securityAdmin.getOriginAdmin();
//        执行删除并判断是否是当前的登录的用户应该看当前用户的id是否和删除的id相匹配，如何匹配就不能删除
        Integer localUserId = admin.getId();
        System.out.println("localUserId=" + localUserId);

        boolean flag = adminService.remove(id, localUserId);

        return flag;

    }


    /**
     * @param keyword  查询的字段
     * @param pageNum  起始页，也可以说是从第一页开始的页码  默认为1
     * @param pageSize 每页显示的数量   默认为5
     * @param model    模型
     * @return
     * @throws LoginFailedException
     */
    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            ModelMap modelMap

    ) throws LoginFailedException {

//      调用service方法
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);

//        存入，方便以后调用,也可以不用这个，因为上面使用了@RequestParam来获取参数
//        modelMap.addAttribute("keyword",keyword);

//        存入模型中
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
        return "admin-page";

    }

    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session) throws LoginFailedException {

//        使session失效
        session.invalidate();
        return "redirect:/login/page.html";

    }


    @RequestMapping("/admin/do/login.html")
    public String doLogin(Admin admin, HttpSession session) throws LoginFailedException {

        logger.info("进入到登录验证操作");

//执行登录检查，如果能返回就说明登录成功，账号密码不正确则会抛出异常
        Admin adminTwo = adminService.getAdminByLoginAcct(admin);

//        将登录成功的账户存入Session中
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, adminTwo);
        System.out.println(adminTwo);
        return "redirect:/main/page.html";
    }

    @RequestMapping("/admin/test/pre/filter.json")
    @ResponseBody
    @PreFilter(value = "filterObject%2==0")  //这个注解用来过滤传过来的参数
    public ResultEntity<List<Integer>> saveList(@RequestBody List<Integer> valueList) {
        return ResultEntity.successWithData(valueList);
    }

}
