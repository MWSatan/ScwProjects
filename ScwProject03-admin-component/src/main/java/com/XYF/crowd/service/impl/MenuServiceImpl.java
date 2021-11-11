package com.XYF.crowd.service.impl;

import com.XYF.crowd.entity.Menu;
import com.XYF.crowd.mapper.MenuMapper;
import com.XYF.crowd.service.MenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @username 熊一飞
 */

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Override
    public List<Menu> getAllMenu() {
        QueryWrapper<Menu> qw = new QueryWrapper<>();
        return menuMapper.selectList(qw);
    }

    @Override
    public void addMenu(Menu menu) {
        menuMapper.insert(menu);
    }

    @Override
    public void editMenu(Menu menu) {
        menuMapper.updateById(menu);
    }

    @Override
    public void removeMenu(Integer id) {
        menuMapper.deleteByPrimaryKey(id);
    }
}
