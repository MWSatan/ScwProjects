package com.XYF.crowd.handler;

import com.XYF.crowd.constant.CrowdConstant;
import com.XYF.crowd.entity.po.MemberPO;
import com.XYF.crowd.api.MemberService;
import com.XYF.crowd.entity.vo.MemberLaunchProjectVO;
import com.XYF.crowd.entity.vo.MemberOrderVO;
import com.XYF.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @username 熊一飞
 */

@RestController
public class MemberProviderHandler {

    @Autowired
    private MemberService memberService;

    @RequestMapping("/get/member/launch/project/remote")
    ResultEntity<List<MemberLaunchProjectVO>> getMemberProjectData(@RequestParam("memberId") Integer memberId) {

        try {
            List<MemberLaunchProjectVO> memberLaunchProjectVOList = memberService.getMemberProjectData(memberId);
            return ResultEntity.successWithData(memberLaunchProjectVOList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failWithMessage(e.getMessage());

        }

    }

    @RequestMapping("/del/member/order/remote")
    ResultEntity<String> delOrderProject(@RequestParam("id") Integer id) {
        try {
            memberService.delOrderProject(id);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failWithMessage(e.getMessage());

        }
    }


    @RequestMapping("/get/member/order/data/remote")
    ResultEntity<List<MemberOrderVO>> getMemberOrderData(@RequestParam("memberId") Integer memberId) {

        try {
            List<MemberOrderVO> memberOrderVOList = memberService.getMemberOrderVOData(memberId);
            return ResultEntity.successWithData(memberOrderVOList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failWithMessage(e.getMessage());

        }

    }


    @RequestMapping("/save/member/remote")
    ResultEntity<String> saveMember(@RequestBody MemberPO memberPO) {


        try {
            memberService.saveMember(memberPO);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {

//            java向数据库插入数据异常
            if (e instanceof DuplicateKeyException) {
                return ResultEntity.failWithMessage(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }

            return ResultEntity.failWithMessage(e.getMessage());

        }

    }

    @RequestMapping("/get/memberpo/by/login/acct/remote")
    ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginAcct") String loginAcct) {

        try {
//            调用本地service来完成查询
            MemberPO memberPO = memberService.getMemberPOByLoginAcct(loginAcct);
            return ResultEntity.successWithData(memberPO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failWithMessage(e.getMessage());

        }
    }


}
