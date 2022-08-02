package com.XYF.crowd.handler;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.XYF.crowd.api.MySqlRemoteService;
import com.XYF.crowd.api.RedisRemoteService;
import com.XYF.crowd.constant.CrowdConstant;
import com.XYF.crowd.entity.po.AddressPO;
import com.XYF.crowd.entity.vo.AddressVO;
import com.XYF.crowd.entity.vo.MemberLoginVO;
import com.XYF.crowd.entity.vo.OrderProjectVO;
import com.XYF.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @username 熊一飞
 */
@Controller
public class OrderConsumerHandler {


    @Autowired
    private MySqlRemoteService mySqlRemoteService;

    @Autowired
    private RedisRemoteService redisRemoteService;

    private Logger logger = LoggerFactory.getLogger(OrderConsumerHandler.class);

    @RequestMapping("/confirm/add/address")
    public String showConfirmOrderInfo(HttpSession session, AddressVO addressVO) {


//        执行地址信息的保存
        ResultEntity<String> resultEntity = mySqlRemoteService.saveAddress(addressVO);

//        取出存储在redis中的returnCount,执行重定向
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT);
        Integer returnCount = orderProjectVO.getReturnCount();

        return "redirect:http://www.xyf2021.ltd/order/confirm/order/info/" + returnCount;
    }

    @RequestMapping("/confirm/order/info/{returnCount}")
    public String showReturnDetail(@PathVariable("returnCount") Integer returnCount, HttpSession session) {

//        1.获得orderProjectVO对象
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT);

//        2.将新的returnCount存入orderProjectVO对象中，并将orderProjectVO对象重新保存至redis中
        orderProjectVO.setReturnCount(returnCount);
        session.setAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT, orderProjectVO);


//        3.获取redis中的member对象的id
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_MEMBER);
        Integer memberId = memberLoginVO.getId();

//        4.根据memberId来查找订单地址
//        地址很可能有多条
        ResultEntity<List<AddressPO>> addressResultEntity = mySqlRemoteService.getAddressData(memberId);

        if (ObjectUtils.nullSafeEquals(addressResultEntity.getResult(), ResultEntity.SUCCESS)) {
//            将地址存入redis中
            List<AddressPO> addressPOList = addressResultEntity.getData();
            session.setAttribute(CrowdConstant.ATTR_NAME_ADDRESS_LIST, addressPOList);
        }

        return "confirm-address";
    }

    @RequestMapping("/confirm/return/info/{projectId}/{returnId}")
    public String showReturnDetail(@PathVariable Integer projectId, @PathVariable Integer returnId, Model model, HttpSession session) {

        ResultEntity<OrderProjectVO> projectVOResultEntity = mySqlRemoteService.getOrderProjectVORemote(projectId, returnId);

        if (ObjectUtils.nullSafeEquals(projectVOResultEntity.getResult(), ResultEntity.SUCCESS)) {
            OrderProjectVO orderProjectVO = projectVOResultEntity.getData();
            orderProjectVO.setProjectId(projectId);
//            将查询出来的数据放至redis中，方便以后完善PO表
            session.setAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT, orderProjectVO);
        }
        return "confirm-return";
    }
}
