package com.XYF.crowd.handler;

import com.XYF.crowd.api.ProjectService;
import com.XYF.crowd.entity.vo.DetailProjectVO;
import com.XYF.crowd.entity.vo.OrderProjectVO;
import com.XYF.crowd.entity.vo.PortalTypeVO;
import com.XYF.crowd.util.ResultEntity;
import com.XYF.crowd.entity.vo.ProjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @username 熊一飞
 */

@RestController
public class ProjectProviderHandler {

    @Autowired
    private ProjectService projectService;

    private Logger logger= LoggerFactory.getLogger(ProjectProviderHandler.class);

    @RequestMapping("/save/project/remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO,
                                             @RequestParam("memberId") Integer memberId) {
        try {
            //没有必要再进行判断是否出错，try一下，有情况直接往上面抛出异常
            projectService.saveProject(projectVO, memberId);

            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failWithMessage("保存失败,也许有标签或者物品没有选到，请回去检查一下");

        }

    }

    @RequestMapping("/get/portal/type/data/remote")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeData() {

        try {
            //没有必要再进行判断是否出错，try一下，有情况直接往上面抛出异常
            List<PortalTypeVO> portalTypeVOList = projectService.getPortalTypeData();
            return ResultEntity.successWithData(portalTypeVOList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failWithMessage(e.getMessage());

        }

    }

    @RequestMapping("/get/detail/project/data/remote")
    public ResultEntity<DetailProjectVO> getDetailProjectData(@RequestParam("projectId") Integer projectId) {

        try {
            //没有必要再进行判断是否出错，try一下，有情况直接往上面抛出异常
            DetailProjectVO detailProjectVO = projectService.getDetailProject(projectId);
            return ResultEntity.successWithData(detailProjectVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failWithMessage(e.getMessage());
        }
    }


}