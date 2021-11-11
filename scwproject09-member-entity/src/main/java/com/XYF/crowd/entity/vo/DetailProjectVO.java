package com.XYF.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @username 熊一飞
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailProjectVO implements Serializable {

    private Integer projectId;
    //    项目标题
    private String projectName;
    //    项目细节
    private String projectDesc;
    //    关注的人数
    private Integer followerCount;
    private Integer status;
    private String statusText;
    private Integer money;
    private Integer supportMoney;

    private Integer percentage;
    private String launchDate;
    private Integer lastDay;
    private Integer supporterCount;
    private String headerPicturePath;
    private Integer day;
//    详情图片
    private List<String> detailPicturePathList;
    //    项目明细
    private List<DetailReturnVO> detailReturnVOList;
}
