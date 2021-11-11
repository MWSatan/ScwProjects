package com.XYF.crowd.mvc.config;

import com.XYF.crowd.entity.Admin;
import com.XYF.crowd.entity.Role;
import com.XYF.crowd.service.AdminService;
import com.XYF.crowd.service.AuthService;
import com.XYF.crowd.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @username 熊一飞
 */

//作为bean注入到security的配置类中
@Component
public class CrowdUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthService authService;

    @Autowired
    private RoleService roleService;




    @Override
    public UserDetails loadUserByUsername(String login_acct) throws UsernameNotFoundException {
//      1.根据登录名称查询Admin对象
        Admin admin = adminService.getAdminByLoginAcct(login_acct);



//        2.获取adminId
        Integer adminId = admin.getId();


//        3.根据adminId查询已分配的角色信息
        List<Role> roleList = roleService.getAssignRole(adminId);

//        4.根据adminId查询权限的信息
        List<String> AuthList = authService.getAssignAuthNameByRoleId(adminId);

//        5.创建集合对象用来存储GrantedAuthority
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();

//        6.遍历roleList存入角色信息
        roleList.forEach(role -> {
//            注意：这里我犯了一个毛病，把role存入进去了，应该是角色名存入进去
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getName());
            grantedAuthorityList.add(authority);
        });

//        7.遍历AuthList存入权限信息
        AuthList.forEach(auth -> {
            GrantedAuthority authority = new SimpleGrantedAuthority(auth);
            grantedAuthorityList.add(authority);
        });


//       8.封装SecurityAdmin对象,将查询到的Admin对象和封装好的grantedAuthorityList传入
        SecurityAdmin securityAdmin = new SecurityAdmin(admin,grantedAuthorityList);

        /*
        就会变成这样的形式
        user user:delete
        user user:经理
         */

//        返回securityAdmin(User)对象
        return securityAdmin;
    }
}
