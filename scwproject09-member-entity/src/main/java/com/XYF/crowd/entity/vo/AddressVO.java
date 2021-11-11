package com.XYF.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @username 熊一飞
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressVO implements Serializable {

    private String receiveName;

    private String phoneNum;

    private String address;

    private Integer memberId;
}
