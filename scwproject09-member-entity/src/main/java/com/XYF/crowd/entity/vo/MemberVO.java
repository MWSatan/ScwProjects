package com.XYF.crowd.entity.vo;

/**
 * @username 熊一飞
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 该vo类封装的是注册表单的信息
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO {

    private String loginacct;

    private String username;
    private String email;


    private String userpswd;

    private String phone;

    private String identifyCode;


}
