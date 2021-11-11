package com.XYF.crowd.mapper;


import com.XYF.crowd.entity.po.OrderPO;
import com.XYF.crowd.entity.po.OrderExample;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper extends BaseMapper<OrderPO> {
    int countByExample(OrderExample example);

    int deleteByExample(OrderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OrderPO record);

    int insertSelective(OrderPO record);

    List<OrderPO> selectByExample(OrderExample example);

    OrderPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OrderPO record, @Param("example") OrderExample example);

    int updateByExample(@Param("record") OrderPO record, @Param("example") OrderExample example);

    int updateByPrimaryKeySelective(OrderPO record);

    int updateByPrimaryKey(OrderPO record);
}