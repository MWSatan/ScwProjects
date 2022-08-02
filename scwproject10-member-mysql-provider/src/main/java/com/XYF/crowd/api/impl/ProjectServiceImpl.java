package com.XYF.crowd.api.impl;

import com.XYF.crowd.api.ProjectService;
import com.XYF.crowd.entity.po.MemberConfirmInfoPO;
import com.XYF.crowd.entity.po.MemberLaunchInfoPO;
import com.XYF.crowd.entity.po.ProjectPO;
import com.XYF.crowd.entity.po.ReturnPO;
import com.XYF.crowd.entity.vo.*;
import com.XYF.crowd.mapper.*;
import com.XYF.crowd.util.ResultEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @username 熊一飞
 */

@Service
@SuppressWarnings("all")
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Autowired
    private TypePOMapper typePOMapper;

    @Autowired
    private TagPOMapper tagPOMapper;

    @Autowired
    private ProjectItemPicPOMapper picPOMapper;

    @Autowired
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;

    @Autowired
    private ReturnPOMapper returnPOMapper;

    @Autowired
    private MemberConfirmInfoPOMapper confirmInfoPOMapper;

    private Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class,
            readOnly = false)
    public void saveProject(ProjectVO projectVO, Integer memberId) {


//        一、保存ProjectVO对象
//        1.创建空的projectPO对象
        ProjectPO projectPO = new ProjectPO();

//        2.把projectVO中的属性赋赋值到projecyPO对象中
        BeanUtils.copyProperties(projectVO, projectPO);

        logger.error(projectPO.toString());
//        补：加上projectPO缺少的字段
        projectPO.setMemberid(memberId);

//        补，生成创建时间，不必精确到秒
        String createTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        projectPO.setCreatedate(createTime);

//        补：设置status，0为即将开始，1位众筹中
        projectPO.setStatus(1);

//        设置部署日期,部署日期就是创建日期
        projectPO.setDeploydate(createTime);

//        支持金额初始化为0
        projectPO.setSupportmoney((long) 0);


//        3.保存projectPO
//       为了能够获得projectPO保存后的自增主键，需要到ProjectPOMapper.xml文件中进行相关设置
        projectPOMapper.insertSelective(projectPO);


        Integer projectId = projectPO.getId();

        logger.error(projectPO.toString());


//      二、保存项目、分类的关联关系信息
//        1、获得分类的关联信息id
        List<Integer> typeIdList = projectVO.getTypeIdList();
        projectPOMapper.insertTypeRelationShip(typeIdList, projectId);

//       三、保存项目、标签的关联关系信息
        List<Integer> tagIdList = projectVO.getTagIdList();
        tagPOMapper.insertTagRelationShip(tagIdList, projectId);
//        四、保存项目中详情图片路径信息
        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
        picPOMapper.insertPicRelationShip(detailPicturePathList, projectId);

//        五、保存项目发起人的信息
//        保存的是一个对象，而且需要调用发起者mapper接口，因为发起者mapper文件对应的是PO对象，所以需要new一个PO对象来保存发起人信息
        MemberLauchInfoVO memberLauchInfoVO = projectVO.getMemberLauchInfoVO();
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
        BeanUtils.copyProperties(memberLauchInfoVO, memberLaunchInfoPO);

        memberLaunchInfoPO.setMemberid(memberId);
        memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);

//        六、保存项目回报信息

        List<ReturnVO> returnVOList = projectVO.getReturnVOList();
        List<ReturnPO> returnPOList = new ArrayList<>();
        returnVOList.forEach(returnVO -> {
            ReturnPO returnPO = new ReturnPO();
            BeanUtils.copyProperties(returnVO, returnPO);
            returnPOList.add(returnPO);
        });

        returnPOMapper.insertReturnRelationShip(projectId, returnPOList);

//        七、保存项目确认信息
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
        memberConfirmInfoPO.setPaynum(memberConfirmInfoVO.getPayNum());
        memberConfirmInfoPO.setCardnum(memberConfirmInfoVO.getCardNum());
        memberConfirmInfoPO.setMemberid(memberId);
        confirmInfoPOMapper.insert(memberConfirmInfoPO);
    }

    @Override

    public List<PortalTypeVO> getPortalTypeData() {
        return projectPOMapper.selectPortalTypeVOList();
    }

    /**
     * 获得 statusText 、lastDay
     *
     * @param projectId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class,
            readOnly = false)
    public DetailProjectVO getDetailProject(Integer projectId) {

        QueryWrapper<ProjectPO> qw = new QueryWrapper<>();
//        1.获得查询的DetailProjectVO对象
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);

//        2.根据状态码来设置状态信息
        /*
        0  审核中
        1 众筹中
        2 众筹成功
        3 已关闭
         */
        switch (detailProjectVO.getStatus()) {
            case 0:
                detailProjectVO.setStatusText("审核中");
                break;
            case 1:
                detailProjectVO.setStatusText("众筹中");
                break;
            case 2:
                detailProjectVO.setStatusText("众筹成功");
                break;
            case 3:
                detailProjectVO.setStatusText("众筹失败");
                break;
        }

//        3、根据launchDay计算lastday
        String launchDay = detailProjectVO.getLaunchDate();
//        获得当前时间
        Date currentDay = new Date();
//        将launchDay转为日期类
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date launchDate = simpleDateFormat.parse(launchDay);

//       获得发布日期的时间戳以及当前时间的时间戳
            long lanchTime = launchDate.getTime();
            long currentTime = currentDay.getTime();

//            获得耗费的天数，(当前时间-发布时间) * 当前的毫秒数(1000毫秒/60秒/60分/24时）
            long spendDay = (currentTime - lanchTime) / 1000 / 60 / 60 / 24;

//            获得该项目剩余的天数
            Integer lastDay = (int) (detailProjectVO.getDay() - spendDay);

            qw.eq("id", projectId);
            ProjectPO projectPO = projectPOMapper.selectOne(qw);
            int percentage = (int) (projectPO.getSupportmoney() / projectPO.getMoney() * 100);

            if (lastDay > 0) {
                projectPO.setStatus(1);
                detailProjectVO.setLastDay(lastDay);
            }

            if (lastDay == 0 && percentage >= 100) {
                projectPO.setStatus(2);
            }

            if (lastDay == 0 && percentage < 100) {
                projectPO.setStatus(3);
            }
            projectPOMapper.updateById(projectPO);

        } catch (ParseException e) {
            e.printStackTrace();
        }

//        返回
        return detailProjectVO;
    }
}
