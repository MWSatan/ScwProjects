package com.XYF.crowd.mapper;

import com.XYF.crowd.entity.po.TagPO;
import com.XYF.crowd.entity.po.TagPOExample;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagPOMapper extends BaseMapper<TagPO> {
    int countByExample(TagPOExample example);

    int deleteByExample(TagPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TagPO record);

    int insertSelective(TagPO record);

    List<TagPO> selectByExample(TagPOExample example);

    TagPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TagPO record, @Param("example") TagPOExample example);

    int updateByExample(@Param("record") TagPO record, @Param("example") TagPOExample example);

    int updateByPrimaryKeySelective(TagPO record);

    int updateByPrimaryKey(TagPO record);

    void insertTagRelationShip(@Param("tagList")List<Integer> tagIdList,@Param("projectId") Integer projectId);

}