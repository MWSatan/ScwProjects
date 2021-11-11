package com.XYF.crowd.service;

import com.XYF.crowd.entity.Admin;
import com.XYF.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @username 熊一飞
 */
public interface AdminService {

    void saveAdmin(Admin admin);

    List<Admin> getAll();

    Admin getAdminByLoginAcct(Admin admin);

    PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize);

    boolean remove(Integer integer, Integer id);

    Admin getAdminById(Integer adminId);

    void editAdmin(Admin admin);

    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList);

    Admin getAdminByLoginAcct(String username);
}
