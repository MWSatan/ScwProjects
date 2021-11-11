package com.XYF.crowd.mapper;


import java.util.List;

import com.XYF.crowd.entity.po.AddressPO;
import com.XYF.crowd.entity.po.AddressExample;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface AddressMapper extends BaseMapper<AddressPO> {
    int countByExample(AddressExample example);

    int deleteByExample(AddressExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AddressPO record);

    int insertSelective(AddressPO record);

    List<AddressPO> selectByExample(AddressExample example);

    AddressPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AddressPO record, @Param("example") AddressExample example);

    int updateByExample(@Param("record") AddressPO record, @Param("example") AddressExample example);

    int updateByPrimaryKeySelective(AddressPO record);

    int updateByPrimaryKey(AddressPO record);
}