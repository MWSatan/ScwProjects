package com.XYF.crowd.service;

import com.XYF.crowd.entity.Menu;

import java.util.List;

/**
 * @username 熊一飞
 */
public interface MenuService {

    List<Menu> getAllMenu();

    void addMenu(Menu menu);

    void editMenu(Menu menu);

    void removeMenu(Integer id);
}
