package com.XYF.crowd.service;

import com.XYF.crowd.entity.Auth;

import java.util.List;
import java.util.Map;

/**
 * @username 熊一飞
 */
public interface AuthService {
    List<Auth> getAll();

    List<Integer> getAssignAuthIdByRoleId(Integer roleId);

    void saveAssignRoleAuthRelationShip(Map<String, List<Integer>> map);

    List<String> getAssignAuthNameByRoleId(Integer adminId);
}
