package com.XYF.crowd.mvc.handler;

import com.XYF.crowd.entity.Auth;
import com.XYF.crowd.entity.Role;
import com.XYF.crowd.service.AdminService;
import com.XYF.crowd.service.AuthService;
import com.XYF.crowd.service.RoleService;
import com.XYF.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @username 熊一飞
 */

@Controller
public class AssignHandler {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    //执行分配权限操作
    @RequestMapping("/assign/do/role/assign/auth.json")
    @ResponseBody
    @PreAuthorize("hasRole('超级管理员')")
    public ResultEntity<String> saveAssignRoleAuthRelationShip(
            @RequestBody Map<String, List<Integer>> map
    ) {

        authService.saveAssignRoleAuthRelationShip(map);
        return ResultEntity.successWithoutData();
    }

    //    使用中间表inner_role_auth，通过roleId来查询auth_id
    @RequestMapping("/assign/get/assigned/auth/id/by/role/id.json")
    @ResponseBody
    @PreAuthorize("hasRole('超级管理员')")
    public ResultEntity<List<Integer>> getAssignAuthIdByRoleId(
            @RequestParam(value = "roleId") Integer roleId
    ) {

        List<Integer> authIdList = authService.getAssignAuthIdByRoleId(roleId);
        return ResultEntity.successWithData(authIdList);

    }

    //    将数据库t_auth的所有数据都查询出来
    @RequestMapping("/get/all/auth.json")
    @ResponseBody
    @PreAuthorize("hasRole('超级管理员')")
    public ResultEntity<List<Auth>> getAllAssignRole() {

        List<Auth> authList = authService.getAll();
        return ResultEntity.successWithData(authList);

    }

    @RequestMapping("/assign/save/assign/role/page.html")
    @PreAuthorize("hasRole('超级管理员')")
    public String saveAssignRole(
            @RequestParam(value = "pageNum") Integer pageNum,
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "adminId") Integer adminId,
            // 我们允许用户在页面上取消所有已分配角色再提交表单，所以可以不提供roleIdList 请求参数
            // 设置 required=false 表示这个请求参数不是必须的
            @RequestParam(value = "roleIdList", required = false) List<Integer> roleIdList

    ) {

        System.out.println("pageNum===" + pageNum);

//        执行保存
        adminService.saveAdminRoleRelationship(adminId, roleIdList);

        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;

    }


    @RequestMapping("/assign/to/assign/role/page.html")
    @PreAuthorize("hasRole('超级管理员')")
    public String toAssignRole(
            @RequestParam("adminId") Integer adminId,
            ModelMap modelMap
    ) {


//        1.查询已分配的角色
        List<Role> assignRoleList = roleService.getAssignRole(adminId);

//        2.查询未分配的角色
        List<Role> unAssignRoleList = roleService.getUnAssignRole(adminId);

//        3.存入模型

        modelMap.addAttribute("assignRoleList", assignRoleList);
        modelMap.addAttribute("unAssignRoleList", unAssignRoleList);


        return "assign-role-page";

    }

}
