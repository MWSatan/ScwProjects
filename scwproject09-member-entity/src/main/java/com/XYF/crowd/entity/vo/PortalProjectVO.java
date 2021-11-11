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
public class PortalProjectVO {

    private Integer projectId;
    private String projectName;
    private Integer money;
//    发布日期
    private String launchDate;
//    完成的百分比
    private Integer percentage;
//    支持的人数
    private Integer supporter;

    private String headerPicturePath;

}
