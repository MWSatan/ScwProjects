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
public class MemberLaunchProjectVO {

    private Integer projectId;
    private Integer memberId;
    private String projectName;
    private String launchDate;
    private Integer status;
    private Double percentage;
    private Integer day;
    private int deadLine;
    private String statusText;
    private Long money;
    private Integer freight;


}
