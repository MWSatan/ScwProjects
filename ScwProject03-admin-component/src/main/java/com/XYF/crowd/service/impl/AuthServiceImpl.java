package com.XYF.crowd.service.impl;

import com.XYF.crowd.entity.Auth;
import com.XYF.crowd.mapper.AuthMapper;
import com.XYF.crowd.service.AuthService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @username 熊一飞
 */

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    @Override
    public List<Auth> getAll() {
        return authMapper.selectList(new QueryWrapper<Auth>());
    }

    @Override
    public List<Integer> getAssignAuthIdByRoleId(Integer roleId) {
        return authMapper.getAssignAuthIdByRoleId(roleId);
    }

    @Override
    public void saveAssignRoleAuthRelationShip(Map<String, List<Integer>> map) {
//        获取roleId,因为我们统一使用了List集合，所以需要List集合来接收
        List<Integer> roleIdList = map.get("roleId");
//        里面也只是传了一个值
        Integer roleId = roleIdList.get(0);
//        删除旧的关联关系数据
        authMapper.deleteOldRelationShip(roleId);

//        获取authIdList
        List<Integer> authIdList = map.get("authIdArray");

//        判断authList是否有效
        if ( authIdList !=null && authIdList.size() >0){
            authMapper.insertRoleAuthRelationship(roleId,authIdList);
        }

    }

    @Override
    public List<String> getAssignAuthNameByRoleId(Integer adminId) {
        return authMapper.selectAssignAuthNameByRoleId(adminId);
    }
}
