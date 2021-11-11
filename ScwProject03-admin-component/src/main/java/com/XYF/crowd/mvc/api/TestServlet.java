package com.XYF.crowd.mvc.api;

import com.XYF.crowd.entity.Admin;
import com.XYF.crowd.service.AdminService;
import com.XYF.crowd.util.CrowdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @username 熊一飞
 */

@Controller
public class TestServlet {

    @Autowired
    private AdminService adminService;

    private Logger logger = LoggerFactory.getLogger(TestServlet.class);


    @RequestMapping("/test/ajax/async.html")
    @ResponseBody
    public String testAsync() throws InterruptedException {
        Thread.sleep(5000);
        return "SUCCESS";
    }

    @RequestMapping("/test/testForm.html")
    public String testForm(@RequestBody String data, HttpServletRequest request) {
        logger.info("提示，已经进入TestForm");

        System.out.println("data---->  " + data);

        boolean flag = CrowdUtil.judgeRequestType(request);

        logger.info("FLAG---->  " + flag);

        String a = null;
        System.out.println(a.length());

        return "result";
    }


    @RequestMapping("/test/testServlet.html")
    public String testMVC(Model model, HttpServletRequest request) {


        List<Admin> admins = adminService.getAll();


        model.addAttribute("adminList", admins);
        logger.info("提示，已经进入MVC");

        boolean flag = CrowdUtil.judgeRequestType(request);

        logger.info("FLAG---->  " + flag);
        System.out.println(10/0);
        return "result";
    }

    @RequestMapping("/test/testAjax.html")
    @ResponseBody
    public String testAjax(@RequestBody Integer[] arrayStr) {
        logger.info("提示，已经进入TestAjax");

        for (Integer a : arrayStr) {
            System.out.println("number--->  " + a);
        }
        return "result";
    }


}

