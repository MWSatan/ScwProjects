package com.XYF.crowd.service;

import com.XYF.crowd.entity.Admin;
import com.XYF.crowd.entity.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @username 熊一飞
 */
public interface RoleService {

    List<Role>  getAllRole();

    PageInfo<Role> getPageInfoRole(String keyword, Integer pageNum, Integer pageSize);

    void addRoleName(Role role);

    boolean editRoleName(Role role);

    void removeRole(List<Integer> roleIds);

    List<Role> getAssignRole(Integer adminId);

    List<Role> getUnAssignRole(Integer adminId);

}
