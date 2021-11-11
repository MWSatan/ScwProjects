package com.XYF.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @username 熊一飞
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderProjectVO implements Serializable {

    private Integer id;

    private String projectName;

    private String launchName;

    private String returnContent;

    private Integer returnCount;

    private Integer supportPrice;

    private Integer freight;

    private Integer orderId;

//    是否设置单笔限购
    private Integer signalPurchase;

//    具体限购数量
    private Integer purchase;

//    该项目的id
    private Integer projectId;

}
