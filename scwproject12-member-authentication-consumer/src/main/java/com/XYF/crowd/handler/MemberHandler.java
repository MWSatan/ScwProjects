package com.XYF.crowd.handler;

import com.XYF.crowd.api.MySqlRemoteService;
import com.XYF.crowd.api.RedisRemoteService;
import com.XYF.crowd.config.ShortMessageProperties;
import com.XYF.crowd.constant.CrowdConstant;
import com.XYF.crowd.entity.po.MemberPO;
import com.XYF.crowd.entity.vo.MemberLaunchProjectVO;
import com.XYF.crowd.entity.vo.MemberOrderVO;
import com.XYF.crowd.util.CrowdUtil;
import com.XYF.crowd.util.MD5Util;
import com.XYF.crowd.util.ResultEntity;
import com.XYF.crowd.entity.vo.MemberLoginVO;
import com.XYF.crowd.entity.vo.MemberVO;
import com.netflix.ribbon.proxy.annotation.Http;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @username 熊一飞
 */

@Controller
public class MemberHandler {

    @Autowired
    private ShortMessageProperties shortMessage;

    @Autowired
    private RedisRemoteService redisRemoteService;

    @Autowired
    private MySqlRemoteService mySqlRemoteService;


    private Logger logger = LoggerFactory.getLogger(MemberHandler.class);

    //    加载发起的项目信息
    @RequestMapping("/member/my/launch/project")
    @ResponseBody
    public ResultEntity<List<MemberLaunchProjectVO>> getMemberProject(HttpSession httpSession, Model model) {

//        获得当前登录的用户id
        MemberLoginVO loginVO = (MemberLoginVO) httpSession.getAttribute(CrowdConstant.ATTR_NAME_MEMBER);
        Integer memberId = loginVO.getId();

//        调用接口查询信息
        ResultEntity<List<MemberLaunchProjectVO>> resultEntity = mySqlRemoteService.getMemberProjectData(memberId);

        /*if (ObjectUtils.nullSafeEquals(resultEntity.getResult(), ResultEntity.SUCCESS)) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MEMBER_PROJECT, resultEntity.getData());
        }*/
        return resultEntity;

    }

    @RequestMapping("/del/order/project/{id}/{memberId}")
    @ResponseBody
    public ResultEntity<String> delOrderProject(@RequestBody @PathVariable Integer id, @RequestBody @PathVariable Integer memberId, HttpSession session) {

        //        获得当前登录的用户id
        MemberLoginVO loginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_MEMBER);

        if (loginVO.getId().equals(memberId)) {

            ResultEntity<String> resultEntity = mySqlRemoteService.delOrderProject(id);
            if (resultEntity.getResult().equals(ResultEntity.SUCCESS)) {
                return ResultEntity.successWithoutData();
            }
        } else {
            return ResultEntity.failWithMessage("删除出错，您不是当前项目的用户，无法删除");

        }
        return ResultEntity.successWithoutData();
    }

    //    加载支持的订单信息
    @RequestMapping("/member/my/crowd/page.html")
    public String getOrderProjectData(HttpSession httpSession, Model model) {

//        获得当前登录的用户id
        MemberLoginVO loginVO = (MemberLoginVO) httpSession.getAttribute(CrowdConstant.ATTR_NAME_MEMBER);
        Integer memberId = loginVO.getId();

//        调用接口查询信息
        ResultEntity<List<MemberOrderVO>> resultEntity = mySqlRemoteService.getMemberOrderData(memberId);

        if (ObjectUtils.nullSafeEquals(resultEntity.getResult(), ResultEntity.SUCCESS)) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MEMBER_ORDER, resultEntity.getData());
        }
        return "member-crowd";

    }

    @RequestMapping("/auth/member/to/logout")
    public String logout(HttpSession httpSession) {

        httpSession.invalidate();
        return "redirect:http://www.crowd.com";
    }

    @RequestMapping("/auth/member/do/login")
    public String login(@RequestParam("loginacct") String loginacct, @RequestParam("userpswd") String
            userpswd, Model model, HttpSession httpSession) {


        if (StringUtils.isEmpty(loginacct) || StringUtils.isEmpty(userpswd)) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_ISNULL);
            return "member-login";
        }

        ResultEntity<MemberPO> mysqlResultEntity = mySqlRemoteService.getMemberPOByLoginAcctRemote(loginacct);


        if (ObjectUtils.nullSafeEquals(mysqlResultEntity.getResult(), ResultEntity.FAIL)) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAiLIED);
            return "member-login";
        }

        MemberPO memberPO = mysqlResultEntity.getData();

        if (memberPO == null) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_MEMBER_NOTEXISTS);
            return "member-login";
        }

//        security加密对象
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//        将表单传过来的密码和使用账户传过来的密码相比对，使用了security的加密需要使用PasswordEncoder对象的对比方法进行对比
        if (passwordEncoder.matches(userpswd, memberPO.getUserpswd())) {
//            登录成功将查到的用户信息传入session中，只有username、id以及email;
            MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail());
            httpSession.setAttribute(CrowdConstant.ATTR_NAME_MEMBER, memberLoginVO);
//            如果你是使用域名登录这个的话，需要在路径前面加入域名，否则它还是跳转到localhost:4000中，那么cookie就无法传达
            return "redirect:http://www.crowd.com/auth/member/to/center/page.html";
//            return "member-center";
        } else {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAiLIED);
            return "member-login";
        }


    }


    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVo, ModelMap modelMap) {

//        1.获取用户手机号
        String phoneNum = memberVo.getPhone();

//        2.拼接redis中存储的key
        String key = CrowdConstant.REDIS_CODE_EFFECTIVE + phoneNum;

//        3.读取key对应的value
        ResultEntity<String> redisResultEntity = redisRemoteService.getRedisValueRemoteByKey(key);

//        4.检查查询操作是否有效
        //获取验证码
        String redisCode = redisResultEntity.getData();

//        返回信息必须是SUCCESS并且value不能为null
        if (ObjectUtils.nullSafeEquals(redisResultEntity.getResult(), ResultEntity.SUCCESS)) {

//            验证码不能为空
            if (!StringUtils.isEmpty(redisCode)) {
//        5.如果从Redis能够查询到value则比较表单验证码和redis验证码

                String formCode = memberVo.getIdentifyCode();
                if (ObjectUtils.nullSafeEquals(redisCode, formCode)) {
//        6.一致的话删除redis中的验证码，代表这个验证码已经成功使用并且作废

//                    先慢着放开，不要浪费短信接口的机会
//                    redisRemoteService.removeByKey(key);

//        7.执行密码加密,使用带盐值加密
                    String saltPsw = MD5Util.setSaltPasswordEncoder(memberVo.getUserpswd());
                    memberVo.setUserpswd(saltPsw);

//        8.密码加密之后执行保存,将VO的部分属性传入PO中，使用BeanUtils.copyProperties
                    MemberPO memberPO = new MemberPO();
                    BeanUtils.copyProperties(memberVo, memberPO);


//                    REDIS_CODE_EFFECTIVE19170217695

//                    调用远程方法
                    ResultEntity<String> mysqlResultEntity = mySqlRemoteService.saveMember(memberPO);

//                    保存失败的情况,可能是账户已经存在
                    if (ObjectUtils.nullSafeEquals(ResultEntity.FAIL, mysqlResultEntity.getResult())) {
                        modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
                        return "member-reg";
                    }
                } else {
                    //                验证码错误
                    modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.SHORT_MESSAGE_ERROR);
                    return "member-reg";
                }
            }
        } else {
//           验证码发送失败，就没有验证码，所以直接返回错误信息，所以不需要再做没有验证码的操作
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, redisResultEntity.getMessage());
            return "member-reg";
        }

//        避免刷新浏览器导致重新再执行注册
        return "redirect:http://www.crowd.com/auth/member/to/login/page.html";
    }

    @RequestMapping("/auth/member/send/short/message.json")
    @ResponseBody
    public ResultEntity<String> sendShortMessage(@RequestParam("phoneNum") String phoneNumber) {

//         1.发送验证码到phoneNum手机
        ResultEntity<String> resultEntity = CrowdUtil.sendMessage(shortMessage.getCustomizeHost(),
                shortMessage.getCustomizePath(), shortMessage.getCustomizeMethod(), phoneNumber,
                shortMessage.getAppCode(),
                shortMessage.getTemplateId(), shortMessage.getSign());


//        2.判断短信结果
        if (ObjectUtils.nullSafeEquals(ResultEntity.SUCCESS, resultEntity.getResult())) {

//        3.发送成功将结果存入redis中
            String value = resultEntity.getData();

//            4、拼接一个key
            String key = CrowdConstant.REDIS_CODE_EFFECTIVE + phoneNumber;

            //该验证码15分钟有效
//           5、调用redis接口将值保存到redis中
            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisValueRemoteTimeOut(key, value, 15, TimeUnit.MINUTES);


//            这个有时候也不是百分百发送成功的
            if (ObjectUtils.nullSafeEquals(ResultEntity.SUCCESS, saveCodeResultEntity.getResult())) {
                return ResultEntity.successWithoutData();
            } else {
//                6、失败返回错误信息
                return saveCodeResultEntity;

            }

        } else {
            //                失败返回错误信息
            return resultEntity;
        }

    }
}
