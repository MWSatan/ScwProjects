package com.XYF.crowd.handler;

import com.XYF.crowd.api.OrderService;
import com.XYF.crowd.api.PayService;
import com.XYF.crowd.entity.po.OrderPO;
import com.XYF.crowd.entity.vo.OrderVO;
import com.XYF.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @username 熊一飞
 */

@RestController
public class PayProviderHandler {

    @Autowired
    private PayService payService;

    @RequestMapping("/save/order/remote")
    ResultEntity<String> saveOrderPORemote(@RequestBody OrderVO orderVO) {
        try {
            payService.saveOrderPO(orderVO);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failWithMessage(e.getMessage());
        }
    }
}
