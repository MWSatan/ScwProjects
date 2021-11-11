package com.XYF.crowd.mapper;


import com.XYF.crowd.entity.po.MemberConfirmInfoPO;
import com.XYF.crowd.entity.po.MemberConfirmInfoPOExample;
import com.XYF.crowd.entity.po.MemberLaunchInfoPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberConfirmInfoPOMapper extends BaseMapper<MemberLaunchInfoPO> {
    int countByExample(MemberConfirmInfoPOExample example);

    int deleteByExample(MemberConfirmInfoPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MemberConfirmInfoPO record);

    int insertSelective(MemberConfirmInfoPO record);

    List<MemberConfirmInfoPO> selectByExample(MemberConfirmInfoPOExample example);

    MemberConfirmInfoPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MemberConfirmInfoPO record, @Param("example") MemberConfirmInfoPOExample example);

    int updateByExample(@Param("record") MemberConfirmInfoPO record, @Param("example") MemberConfirmInfoPOExample example);

    int updateByPrimaryKeySelective(MemberConfirmInfoPO record);

    int updateByPrimaryKey(MemberConfirmInfoPO record);
}