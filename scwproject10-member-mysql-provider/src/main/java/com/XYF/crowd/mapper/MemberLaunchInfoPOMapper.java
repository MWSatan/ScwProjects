package com.XYF.crowd.mapper;


import com.XYF.crowd.entity.po.MemberLaunchInfoPO;
import com.XYF.crowd.entity.po.MemberLaunchInfoPOExample;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberLaunchInfoPOMapper extends BaseMapper<MemberLaunchInfoPO> {
    int countByExample(MemberLaunchInfoPOExample example);

    int deleteByExample(MemberLaunchInfoPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MemberLaunchInfoPO record);

    int insertSelective(MemberLaunchInfoPO record);

    List<MemberLaunchInfoPO> selectByExample(MemberLaunchInfoPOExample example);

    MemberLaunchInfoPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MemberLaunchInfoPO record, @Param("example") MemberLaunchInfoPOExample example);

    int updateByExample(@Param("record") MemberLaunchInfoPO record, @Param("example") MemberLaunchInfoPOExample example);

    int updateByPrimaryKeySelective(MemberLaunchInfoPO record);

    int updateByPrimaryKey(MemberLaunchInfoPO record);
}