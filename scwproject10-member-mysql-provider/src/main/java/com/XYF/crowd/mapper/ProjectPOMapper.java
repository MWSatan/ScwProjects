package com.XYF.crowd.mapper;


import com.XYF.crowd.entity.po.ProjectPO;
import com.XYF.crowd.entity.po.ProjectPOExample;
import com.XYF.crowd.entity.vo.DetailProjectVO;
import com.XYF.crowd.entity.vo.PortalTypeVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectPOMapper extends BaseMapper<ProjectPO> {
    int countByExample(ProjectPOExample example);

    int deleteByExample(ProjectPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProjectPO record);

    int insertSelective(ProjectPO record);

    List<ProjectPO> selectByExample(ProjectPOExample example);

    ProjectPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProjectPO record, @Param("example") ProjectPOExample example);

    int updateByExample(@Param("record") ProjectPO record, @Param("example") ProjectPOExample example);

    int updateByPrimaryKeySelective(ProjectPO record);

    int updateByPrimaryKey(ProjectPO record);

    void insertTypeRelationShip(@Param("typeIdList") List<Integer> typeIdList, @Param("projectId") Integer projectId);

    List<PortalTypeVO> selectPortalTypeVOList();

    DetailProjectVO selectDetailProjectVO(@Param("projectId") Integer projectId);

}