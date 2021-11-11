package com.XYF.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @username 熊一飞
 */

/*
登录检测的VO类，账户和密码为了安全所以不能写入
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginVO  implements Serializable {

    private Integer id;

    private String username;

    private String email;
}
