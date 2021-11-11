package com.XYF.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @username 熊一飞
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailReturnVO {

    //    回报信息主键
    private Integer returnId;

    //    当前档位需支持的金额
    private Integer supportMoney;

    //单笔限购，取值为0的时候无限额，取值为正时为具体限额
    private Integer signalPurchase;

    // 具体限额数量
    private Integer purchase;

    //    当前该档位支持的数量
    private Integer supporterCount;

    //    0包邮，1不包邮
    private Integer freight;

    //   众筹成功后发货的天数
    private Integer deliveryDate;

    //    回报内容
    private String content;

}
