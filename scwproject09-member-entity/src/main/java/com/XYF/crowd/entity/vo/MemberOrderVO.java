package com.XYF.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sun.dc.pr.PRError;

import java.io.Serializable;

/**
 * @username 熊一飞
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberOrderVO implements Serializable {

    private Integer id;
    private String projectName;
    private Double orderAmount;
    private String orderNum;
    private String deployDate;
    private Integer status;
    private Integer returnCount;
    private Integer freight;
    private String supportDate;
    private double percentage;
    private Integer lastDate;
    private String statusText;
    private Integer day;

}
