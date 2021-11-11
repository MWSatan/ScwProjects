package com.XYF.crowd.mvc.config;

import com.XYF.crowd.entity.Admin;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.Collection;

/**
 * @username 熊一飞
 */

/**
 *  考虑到User对象中仅仅包含账号和密码，为了能够获得原始的Admin对象，专门创建这个类对User类进行扩展
 */

//springSecurity中User实现的是UserDetails，而UserDetails是UserDetailsService的返回值
public class SecurityAdmin extends User  {


//    原始的Admin对象
    private Admin originAdmin;


    public SecurityAdmin(
//            创建原始的Admin对象
                         Admin originAdmin,
//                         创建角色、权限信息的集合
                         Collection<? extends GrantedAuthority> authorities) {

//        调用父类的构造器，传入账号密码和授权信息，使用完后自动清除Admin对象
        super(originAdmin.getLoginAcct(), originAdmin.getUserPswd(), authorities);

//        给本类的originAdmin赋值
        this.originAdmin = originAdmin;

//        虽然注入的Admin对象被删除了，但是原始的Admin对象没有被清除，所以登录完成后擦除密码，防止密码泄露
        this.originAdmin.setUserPswd(null);
    }

    public Admin getOriginAdmin() {
        return originAdmin;
    }


}
