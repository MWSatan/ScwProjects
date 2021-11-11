package com.XYF.crowd.handler;

import com.XYF.crowd.api.OrderService;
import com.XYF.crowd.entity.po.AddressPO;
import com.XYF.crowd.entity.po.OrderPO;
import com.XYF.crowd.entity.vo.AddressVO;
import com.XYF.crowd.entity.vo.OrderProjectVO;
import com.XYF.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @username 熊一飞
 */

@RestController //因为这是feign暴露接口，给调用者传输数据，所以得用RestController注解
public class OrderProviderHandler {

    @Autowired
    private OrderService orderService;


    @RequestMapping("/save/address/remote")
    ResultEntity<String> saveAddress(@RequestBody AddressVO addressVO) {

        try {
            orderService.saveAddress(addressVO);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failWithMessage(e.getMessage());
        }

    }


    @RequestMapping("/get/address/data/remote")
    ResultEntity<List<AddressPO>> getAddressData(@RequestParam("memberId") Integer memberId) {

        try {
            List<AddressPO> addressVO = orderService.getAddressVOData(memberId);
            return ResultEntity.successWithData(addressVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failWithMessage(e.getMessage());
        }
    }

    @RequestMapping("/get/order/project/data/remote")
    ResultEntity<OrderProjectVO> getOrderProjectVORemote(@RequestParam("projectId") Integer projectId, @RequestParam("returnId") Integer returnId) {

        try {
            OrderProjectVO orderProjectVO = orderService.getOrderProjectVOData(projectId, returnId);

            return ResultEntity.successWithData(orderProjectVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failWithMessage(e.getMessage());
        }
    }
}
