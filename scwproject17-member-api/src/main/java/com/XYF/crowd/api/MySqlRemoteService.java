package com.XYF.crowd.api;

import com.XYF.crowd.entity.po.AddressPO;
import com.XYF.crowd.entity.po.MemberPO;
import com.XYF.crowd.entity.po.OrderPO;
import com.XYF.crowd.entity.vo.*;
import com.XYF.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @username 熊一飞
 */

//对外暴露接口服务,实现远程调用
@FeignClient("xyf-mysql-provider")
public interface MySqlRemoteService {

    @RequestMapping("/get/memberpo/by/login/acct/remote")
    ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginAcct") String loginAcct);

    @RequestMapping("/save/member/remote")
    ResultEntity<String> saveMember(@RequestBody MemberPO memberPO);


    @RequestMapping("/save/project/remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO,
                                             @RequestParam("memberId") Integer memberId);

    @RequestMapping("/get/portal/type/data/remote")
    ResultEntity<List<PortalTypeVO>> getPortalTypeData();

    @RequestMapping("/get/detail/project/data/remote")
    public ResultEntity<DetailProjectVO> getDetailProjectData(@RequestParam("projectId") Integer projectId);

    @RequestMapping("/get/order/project/data/remote")
    ResultEntity<OrderProjectVO> getOrderProjectVORemote(@RequestParam("projectId") Integer projectId, @RequestParam("returnId") Integer returnId);

    @RequestMapping("/get/address/data/remote")
    ResultEntity<List<AddressPO>> getAddressData(@RequestParam("memberId") Integer memberId);

    @RequestMapping("/save/address/remote")
    ResultEntity<String> saveAddress(@RequestBody AddressVO addressVO);

    @RequestMapping("/save/order/remote")
    ResultEntity<String> saveOrderPORemote(@RequestBody OrderVO orderVO);

    @RequestMapping("/get/member/order/data/remote")
    ResultEntity<List<MemberOrderVO>> getMemberOrderData(@RequestParam("memberId") Integer memberId);

    @RequestMapping("/del/member/order/remote")
    ResultEntity<String> delOrderProject(@RequestParam("id") Integer id);

    @RequestMapping("/get/member/launch/project/remote")
    ResultEntity<List<MemberLaunchProjectVO>> getMemberProjectData(@RequestParam("memberId") Integer memberId);
}
