package com.XYF.crowd.mvc.handler;

import com.XYF.crowd.entity.Menu;
import com.XYF.crowd.service.MenuService;
import com.XYF.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @username 熊一飞
 */

@RestController //等于 在整个类上面 加上@Controller @ResponseBody
@SuppressWarnings("all")
public class MenuHandler {

    @Autowired
    private MenuService menuService;

    private Logger logger = LoggerFactory.getLogger(MenuHandler.class);

    @RequestMapping(value = "/menu/remove/tree.json")
    @PreAuthorize("hasRole('超级管理员')")
    public ResultEntity<String> removeMenu(Integer id) {

        menuService.removeMenu(id);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping(value = "/menu/edit/tree.json")
    public ResultEntity<String> editMenu(Menu menu) {

        menuService.editMenu(menu);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping(value = "/menu/add/tree.json")
    public ResultEntity<String> addMenu(Menu menu) {
        menuService.addMenu(menu);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping(value = "/menu/get/whole/tree.json")
    public ResultEntity<Menu> getWholeTreeNew() {
//        1.查询全部的Menu对象
        List<Menu> menuList = menuService.getAllMenu();

//        2.声明一个变量用来存储找到的根节点
        Menu root = null;

//        3、创建一个Map集合来存储id和menu对象的对应关系便于查找父节点
        Map<Integer, Menu> map = new HashMap<>();

//        4.遍历list集合,不能使用menuList.foreach()方法
        for (Menu menu : menuList) {
//           5、将节点对象通过id来存储在map对象中
            map.put(menu.getId(), menu);
        }

//            /6、继续遍历集合，组装树形结构结构
        for (Menu menu : menuList) {
            //获取当前的pid属性
            Integer pid = menu.getPid();

            // 7、检查pid是否为null，如果是null，说明最顶层的根节点
            if (pid == null) {
//                8.如果是null，说明最顶层的节点，则把当前的menu赋给当前的root
                root = menu;
//             跳过符合条件的参数，继续执行循环
                continue;
            }

//            如果不为null，说明当前节点是有父节点的

//          9.获取当前节点的父节点对象
            Menu father = map.get(pid);

//           10、 将子节点对象存入父节点对象的children方法中
            father.getChildren().add(menu);

        }

        //10、将组装的树形结构返回
        return ResultEntity.successWithData(root);


    }


    public ResultEntity<Menu> getWholeTreeOld() {
//        1.查询全部的Menu对象
        List<Menu> menuList = menuService.getAllMenu();

//        2.声明一个变量用来存储找到的根节点
        Menu root = null;

//        3.遍历list集合,不能使用menuList.foreach()方法
        for (Menu menu : menuList) {
            //           5、 获取当前的pid属性
            Integer pid = menu.getPid();
//            6、检查pid是否为null，如果是null，说明最顶层的父节点
            if (pid == null) {
//                7.如果是null，说明最顶层的节点，则把当前的menu赋给当前的root
                root = menu;
//             跳过符合条件的参数，继续执行循环
                continue;
            }

//            8、如果pid不为null，说明当前节点有父节点，则进行组装，建立父子关系
            for (Menu maybeFather : menuList) {
//                9.获取maybeFather的id属性
                Integer id = maybeFather.getId();

//                10.将当前的pid和父节点的id进行比较
                if (Objects.equals(pid, id)) {

//                 11.将当前节点存入父节点的children集合
                    maybeFather.getChildren().add(menu);

//                    找到后即可停止循环
                    break;
                }
            }
        }

        //将组装的树形结构返回
        return ResultEntity.successWithData(root);


    }

}
