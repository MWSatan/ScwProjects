package com.XYF.crowd.api.impl;

import com.XYF.crowd.api.PayService;
import com.XYF.crowd.entity.po.OrderPO;
import com.XYF.crowd.entity.po.OrderProjectPO;
import com.XYF.crowd.entity.po.ProjectPO;
import com.XYF.crowd.entity.vo.OrderProjectVO;
import com.XYF.crowd.entity.vo.OrderVO;
import com.XYF.crowd.mapper.OrderMapper;
import com.XYF.crowd.mapper.OrderProjectMapper;
import com.XYF.crowd.mapper.ProjectPOMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.BEncoderStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @username 熊一飞
 */

@Service
@Transactional
@SuppressWarnings("all")
public class PayServiceImpl implements PayService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderProjectMapper orderProjectMapper;

    @Autowired
    private ProjectPOMapper projectPOMapper;


    private Logger logger = LoggerFactory.getLogger(PayService.class);

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void saveOrderPO(OrderVO orderVO) {

        QueryWrapper<ProjectPO> qw = new QueryWrapper<>();


        OrderPO orderPO = new OrderPO();
        BeanUtils.copyProperties(orderVO, orderPO);

        OrderProjectPO orderProjectPO = new OrderProjectPO();
        BeanUtils.copyProperties(orderVO.getOrderProjectVO(), orderProjectPO);


        //            更新该项目的金额
        Integer projectId = orderVO.getProjectId();
        qw.eq("id", projectId);
        ProjectPO projectPO = projectPOMapper.selectOne(qw);

        projectPO.setSupportmoney((long) (orderPO.getOrderAmount() + projectPO.getSupportmoney()));
        projectPO.setSupporter(projectPO.getSupporter() + 1);

        projectPOMapper.updateById(projectPO);

//        获得保存orderPO自动生成的主键，并设置为orderProjectPO需要用的外键
        orderMapper.insert(orderPO);
        Integer orderId = orderPO.getId();
        orderProjectPO.setOrderId(orderId);
        orderProjectPO.setMemberId(orderVO.getMemberId());

//        设置支持日期
        String supportDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        orderProjectPO.setSupportDate(supportDate);
        orderProjectMapper.insert(orderProjectPO);


    }
}
