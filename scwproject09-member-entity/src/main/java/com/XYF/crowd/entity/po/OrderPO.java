package com.XYF.crowd.entity.po;

import com.XYF.crowd.entity.vo.OrderProjectVO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("t_order")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class OrderPO {

    @TableId(type = IdType.AUTO, value = "id")
    private Integer id;

    //    订单号
    private String orderNum;

    //    支付宝流水单号
    private String payOrderNum;

    //    订单金额
    private Double orderAmount;

    //    发票
    private Integer invoice;

    //    发票抬头
    private String invoiceTitle;

    //    备注
    private String orderRemark;

    private String addressId;

    private Integer memberId;




}