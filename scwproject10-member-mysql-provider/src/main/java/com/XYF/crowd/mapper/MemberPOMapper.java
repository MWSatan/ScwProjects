package com.XYF.crowd.mapper;


import com.XYF.crowd.entity.po.MemberPO;
import com.XYF.crowd.entity.po.MemberPOExample;
import com.XYF.crowd.entity.vo.MemberLaunchProjectVO;
import com.XYF.crowd.entity.vo.MemberOrderVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberPOMapper extends BaseMapper<MemberPO> {
    int countByExample(MemberPOExample example);

    int deleteByExample(MemberPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MemberPO record);

    int insertSelective(MemberPO record);

    List<MemberPO> selectByExample(MemberPOExample example);

    MemberPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MemberPO record, @Param("example") MemberPOExample example);

    int updateByExample(@Param("record") MemberPO record, @Param("example") MemberPOExample example);

    int updateByPrimaryKeySelective(MemberPO record);

    int updateByPrimaryKey(MemberPO record);

    List<MemberOrderVO> selectMemberOrder(Integer memberId);

    List<MemberLaunchProjectVO> selectMemberProject(Integer memberId);
}