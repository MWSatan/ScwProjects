package com.XYF.crowd.mapper;


import com.XYF.crowd.entity.po.TypePO;
import com.XYF.crowd.entity.po.TypePOExample;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TypePOMapper extends BaseMapper<TypePO> {
    int countByExample(TypePOExample example);

    int deleteByExample(TypePOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TypePO record);

    int insertSelective(TypePO record);

    List<TypePO> selectByExample(TypePOExample example);

    TypePO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TypePO record, @Param("example") TypePOExample example);

    int updateByExample(@Param("record") TypePO record, @Param("example") TypePOExample example);

    int updateByPrimaryKeySelective(TypePO record);

    int updateByPrimaryKey(TypePO record);
}