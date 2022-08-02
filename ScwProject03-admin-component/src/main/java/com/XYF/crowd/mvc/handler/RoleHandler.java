package com.XYF.crowd.mvc.handler;

import com.XYF.crowd.entity.Role;
import com.XYF.crowd.exception.LoginFailedException;
import com.XYF.crowd.service.RoleService;
import com.XYF.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @username 熊一飞
 */

@Controller
public class RoleHandler {

    @Autowired
    private RoleService roleService;

    private Logger logger = LoggerFactory.getLogger(RoleHandler.class);

    @RequestMapping("/role/get/page/info.json")
    @ResponseBody
//    使用注解形式来启用权限访问
    public ResultEntity<PageInfo<Role>> getPageInfo(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            ModelMap modelMap

    ) throws LoginFailedException {

//      调用service方法
        PageInfo<Role> pageInfo = roleService.getPageInfoRole(keyword, pageNum, pageSize);

//        封装到ResultEntity对象返回，如果抛出异常，交给异常处理机制来处理
        return ResultEntity.successWithData(pageInfo);

    }


    @RequestMapping("/role/add/page.json")
    @ResponseBody
    @PreAuthorize("hasRole('超级管理员')||hasAuthority('role:add')")
    public ResultEntity<String> addRole(Role role) throws LoginFailedException {

        if (role.getName().equals("超级管理员")) {
            return ResultEntity.failWithMessage("无法添加超级权限");
        }
//      调用service方法
        roleService.addRoleName(role);


//        封装到ResultEntity对象返回，如果抛出异常，交给异常处理机制来处理
        return ResultEntity.successWithoutData();

    }

    @RequestMapping("/role/edit/page.json")
    @ResponseBody
    @PreAuthorize("hasRole('超级管理员')||hasAuthority('role:edit')")
    public boolean editRole(Role role) throws LoginFailedException {

        if (role.getId()==99){
            return false;
        }
//      调用service方法
        boolean flag = roleService.editRoleName(role);

//        封装到ResultEntity对象返回，如果抛出异常，交给异常处理机制来处理
        return flag;

    }

    @RequestMapping("/role/remove/page.json")
    @ResponseBody
    @PreAuthorize("hasRole('超级管理员')&&hasAuthority('role:delete')")
    public ResultEntity<String> removeRole(@RequestBody List<Integer> roleList) throws LoginFailedException {



        for (Integer id : roleList){
            if (id==99){
                return ResultEntity.failWithMessage("删除失败");
            }
        }
        roleService.removeRole(roleList);
        return ResultEntity.successWithoutData();

    }
}
