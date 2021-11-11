package com.XYF.crowd.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_member_confirm_info")
public class MemberConfirmInfoPO {

    @TableId(type = IdType.AUTO,value="id")
    private Integer id;

    private Integer memberid;

    private String paynum;

    private String cardnum;

}