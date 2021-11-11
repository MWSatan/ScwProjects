package com.XYF.crowd.service.impl;

import com.XYF.crowd.entity.Admin;
import com.XYF.crowd.entity.Role;
import com.XYF.crowd.mapper.RoleMapper;
import com.XYF.crowd.service.RoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @username 熊一飞
 */

@Service
@Transactional
public class RoleServiceImpl implements RoleService {


    @Autowired
    private RoleMapper roleMapper;

    private Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Override
    public List<Role> getAllRole() {

        return roleMapper.selectList(new QueryWrapper<Role>());
    }

    @Override
    public PageInfo<Role> getPageInfoRole(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Role> qw = new QueryWrapper<>();
        qw.like("name", keyword);
        List<Role> roles = roleMapper.selectList(qw);

        return new PageInfo<Role>(roles);
    }

    @Override
    public void addRoleName(Role role) {
        roleMapper.insert(role);
    }

    @Override
    public boolean editRoleName(Role role) {
        boolean flag = true;
        if (role == null || role.getId() == null || role.getName() == null) {
            flag = false;
        } else {
            int count = roleMapper.updateById(role);
            if (count != 1) {
                flag = false;
            }
        }

        return flag;
    }

    @Override
    public void removeRole(List<Integer> roleIds) {
        roleMapper.deleteBatchIds(roleIds);
    }

    @Override
    public List<Role> getAssignRole(Integer adminId) {
        return roleMapper.selectAssignRole(adminId);
    }

    @Override
    public List<Role> getUnAssignRole(Integer adminId) {
        return roleMapper.selectUnAssignRole(adminId);
    }
}
