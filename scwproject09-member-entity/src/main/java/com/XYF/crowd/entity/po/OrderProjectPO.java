package com.XYF.crowd.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("t_order_project")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderProjectPO {

    @TableId(type = IdType.AUTO, value = "id")
    private Integer id;

    private String projectName;

    private String launchName;

    private String returnContent;

    private Integer returnCount;

    private Integer supportPrice;

    private Integer freight;

    private Integer orderId;

    private Integer projectId;

    private Integer memberId;

    private String supportDate;

}