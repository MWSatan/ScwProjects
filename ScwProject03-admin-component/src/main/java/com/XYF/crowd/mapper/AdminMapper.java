package com.XYF.crowd.mapper;

import com.XYF.crowd.entity.Admin;
import com.XYF.crowd.entity.AdminExample;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

public interface AdminMapper extends BaseMapper<Admin> {
    int countByExample(AdminExample example);

    int deleteByExample(AdminExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    int insertSelective(Admin record);

    List<Admin> selectByExample(AdminExample example);

    Admin selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Admin record, @Param("example") AdminExample example);

    int updateByExample(@Param("record") Admin record, @Param("example") AdminExample example);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);

    List<Admin> selectAdminByKeyword(String keyword);

    void delInnerAdminId(Integer adminId);

//    加入@Param注解后，可以在mapper的sql文件中的foreach中的collection里面直接用，格式为collection="roleIdList",不加的话需要使用list集合进行遍历参数
    void insertAdminRoleRelation(@Param("adminId") Integer adminId,@Param("roleIdList") List<Integer> roleIdList);
}