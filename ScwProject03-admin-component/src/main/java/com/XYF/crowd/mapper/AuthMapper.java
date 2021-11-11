package com.XYF.crowd.mapper;


import com.XYF.crowd.entity.Auth;
import com.XYF.crowd.entity.AuthExample;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthMapper extends BaseMapper<Auth> {
    int countByExample(AuthExample example);

    int deleteByExample(AuthExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Auth record);

    int insertSelective(Auth record);

    List<Auth> selectByExample(AuthExample example);

    Auth selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByExample(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByPrimaryKeySelective(Auth record);

    int updateByPrimaryKey(Auth record);

    List<Integer> getAssignAuthIdByRoleId(Integer roleId);

    void deleteOldRelationShip(Integer roleId);

    void insertRoleAuthRelationship(@Param("roleId") Integer roleId, @Param("authIdList") List<Integer> authIdList);

    List<String> selectAssignAuthNameByRoleId(Integer adminId);
}