package com.XYF.crowd.handler;

import com.XYF.crowd.api.MySqlRemoteService;
import com.XYF.crowd.constant.CrowdConstant;
import com.XYF.crowd.entity.vo.PortalTypeVO;
import com.XYF.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.xml.transform.Result;
import java.util.List;

/**
 * @username 熊一飞
 */

@Controller
public class PortalHandler {


    @Autowired
    private MySqlRemoteService mySqlRemoteService;

    //    这里的斜杆/的意思为将这里的页面设为首页
    @RequestMapping("/")
    public String showPortalPage(ModelMap modelMap) {

//        这里实际开发中需要加载数据

        ResultEntity<List<PortalTypeVO>> portalTypeResultEntity = mySqlRemoteService.getPortalTypeData();

        if (ObjectUtils.nullSafeEquals(portalTypeResultEntity.SUCCESS, portalTypeResultEntity.getResult())) {
//            取出数据，并存入模型中
            List<PortalTypeVO> portalTypeVOList = portalTypeResultEntity.getData();
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_PORTAL, portalTypeVOList);
        }
        return "portal";
    }


}
