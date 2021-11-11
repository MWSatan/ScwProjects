package com.XYF.crowd.api.impl;

import com.XYF.crowd.api.OrderService;
import com.XYF.crowd.entity.po.AddressPO;
import com.XYF.crowd.entity.vo.AddressVO;
import com.XYF.crowd.entity.vo.OrderProjectVO;
import com.XYF.crowd.mapper.AddressMapper;
import com.XYF.crowd.mapper.OrderMapper;
import com.XYF.crowd.mapper.OrderProjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @username 熊一飞
 */

@Service
@Transactional(readOnly = true)
@SuppressWarnings("All")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderProjectMapper orderProjectMapper;


    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveAddress(AddressVO addressVO) {
        if (addressVO != null && addressVO.getAddress() != null && addressVO.getPhoneNum() != null && addressVO.getReceiveName() != null && addressVO.getMemberId() != null) {
            AddressPO addressPO = new AddressPO();
            BeanUtils.copyProperties(addressVO, addressPO);
            addressMapper.insert(addressPO);
        }

    }

    @Override
    public OrderProjectVO getOrderProjectVOData(Integer projectId, Integer returnId) {


        return orderProjectMapper.selectOrderProjectVO(returnId);
    }

    @Override
    public List<AddressPO> getAddressVOData(Integer memberId) {
        QueryWrapper<AddressPO> qw = new QueryWrapper<>();
        qw.eq("member_id", memberId);
        List<AddressPO> addressPOList = addressMapper.selectList(qw);
        return addressPOList;
    }

}
