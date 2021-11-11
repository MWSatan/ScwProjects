package com.XYF.crowd.api;

import com.XYF.crowd.entity.vo.DetailProjectVO;
import com.XYF.crowd.entity.vo.PortalTypeVO;
import com.XYF.crowd.entity.vo.ProjectVO;

import java.util.List;

/**
 * @username 熊一飞
 */
public interface ProjectService {
    void saveProject(ProjectVO projectVO, Integer memberId);

    List<PortalTypeVO> getPortalTypeData();

    DetailProjectVO getDetailProject(Integer projectId);
}
