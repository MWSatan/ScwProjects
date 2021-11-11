package com.XYF.crowd.api;

import com.XYF.crowd.entity.po.AddressPO;
import com.XYF.crowd.entity.vo.AddressVO;
import com.XYF.crowd.entity.vo.OrderProjectVO;

import java.util.List;

/**
 * @username 熊一飞
 */
public interface OrderService {


    OrderProjectVO getOrderProjectVOData(Integer projectId, Integer returnId);

    List<AddressPO> getAddressVOData(Integer memberId);

    void saveAddress(AddressVO addressVO);
}
