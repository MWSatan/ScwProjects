package com.XYF.crowd.mapper;


import java.util.List;

import com.XYF.crowd.entity.po.OrderProjectPO;
import com.XYF.crowd.entity.po.OrderProjectExample;
import com.XYF.crowd.entity.vo.OrderProjectVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface OrderProjectMapper extends BaseMapper<OrderProjectPO> {
    int countByExample(OrderProjectExample example);

    int deleteByExample(OrderProjectExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OrderProjectPO record);

    int insertSelective(OrderProjectPO record);

    List<OrderProjectPO> selectByExample(OrderProjectExample example);

    OrderProjectPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OrderProjectPO record, @Param("example") OrderProjectExample example);

    int updateByExample(@Param("record") OrderProjectPO record, @Param("example") OrderProjectExample example);

    int updateByPrimaryKeySelective(OrderProjectPO record);

    int updateByPrimaryKey(OrderProjectPO record);

    OrderProjectVO selectOrderProjectVO(Integer returnId);
}