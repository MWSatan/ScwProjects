package com.XYF.crowd.api.impl;

import com.XYF.crowd.entity.po.OrderProjectPO;
import com.XYF.crowd.entity.vo.MemberLauchInfoVO;
import com.XYF.crowd.entity.vo.MemberLaunchProjectVO;
import com.XYF.crowd.entity.vo.MemberOrderVO;
import com.XYF.crowd.mapper.MemberPOMapper;
import com.XYF.crowd.entity.po.MemberPO;
import com.XYF.crowd.api.MemberService;
import com.XYF.crowd.mapper.OrderProjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @username 熊一飞
 */

@Service
@Transactional(readOnly = true) //启动只读事务
@SuppressWarnings("all")
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Autowired
    private OrderProjectMapper orderProjectMapper;

    public MemberPO getMemberPOByLoginAcct(String loginAcct) {

        QueryWrapper<MemberPO> qw = new QueryWrapper<MemberPO>();
        qw.eq("loginacct", loginAcct);
        return memberPOMapper.selectOne(qw);
    }

    /*
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class,
    readOnly = false)
    这段的意思是开启一个新的事务并且出现异常就回滚（），将只读关闭
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class,
            readOnly = false)
    public void saveMember(MemberPO memberPO) {
        memberPOMapper.insertSelective(memberPO);
    }

    @Override
    public List<MemberOrderVO> getMemberOrderVOData(Integer memberId) {

        List<MemberOrderVO> memberOrderVOList = memberPOMapper.selectMemberOrder(memberId);
        memberOrderVOList.forEach(memberOrderVO -> {


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date launchDate = simpleDateFormat.parse(memberOrderVO.getDeployDate());

//       获得发布日期的时间戳以及当前时间的时间戳
                long lanchTime = launchDate.getTime();
                long currentTime = new Date().getTime();

//            获得耗费的天数，(当前时间-发布时间) * 当前的毫秒数(1000毫秒/60秒/60分/24时）
                long spendDay = (currentTime - lanchTime) / 1000 / 60 / 60 / 24;

//            获得该项目剩余的天数
                Integer lastDay = (int) (memberOrderVO.getDay() - spendDay);


                if (lastDay > 0) {
                    memberOrderVO.setStatus(1);
                    memberOrderVO.setLastDate(lastDay);
                }

                if (lastDay <= 0 && memberOrderVO.getPercentage() >= 100) {
                    memberOrderVO.setStatus(2);
                }

                if (lastDay <= 0 && memberOrderVO.getPercentage() < 100) {
                    memberOrderVO.setStatus(3);
                }


                switch (memberOrderVO.getStatus()) {
                    case 0:
                        memberOrderVO.setStatusText("审核中");
                        break;
                    case 1:
                        memberOrderVO.setStatusText("众筹中");
                        break;
                    case 2:
                        memberOrderVO.setStatusText("众筹成功");
                        break;
                    case 3:
                        memberOrderVO.setStatusText("众筹失败");
                        break;
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }

//            将百分比精确到后两位小数
            BigDecimal b = new BigDecimal(memberOrderVO.getPercentage());
            double percent = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            memberOrderVO.setPercentage(percent);

        });
        return memberOrderVOList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class,
            readOnly = false)
    public void delOrderProject(Integer id) {
        orderProjectMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<MemberLaunchProjectVO> getMemberProjectData(Integer memberId) {

        List<MemberLaunchProjectVO> memberLauchInfoVOList = memberPOMapper.selectMemberProject(memberId);

        memberLauchInfoVOList.forEach(memberLaunchProjectVO -> {




            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date launchDate = simpleDateFormat.parse(memberLaunchProjectVO.getLaunchDate());

//       获得发布日期的时间戳以及当前时间的时间戳
                long lanchTime = launchDate.getTime();
                long currentTime = new Date().getTime();

//            获得耗费的天数，(当前时间-发布时间) * 当前的毫秒数(1000毫秒/60秒/60分/24时）
                long spendDay = (currentTime - lanchTime) / 1000 / 60 / 60 / 24;

//            获得该项目剩余的天数
                Integer lastDay = (int) (memberLaunchProjectVO.getDay() - spendDay);
                if (lastDay > 0) {
                    memberLaunchProjectVO.setStatus(1);
                    memberLaunchProjectVO.setDeadLine(lastDay);
                }

                if (lastDay <= 0 && memberLaunchProjectVO.getPercentage() >= 100) {
                    memberLaunchProjectVO.setStatus(2);
                }

                if (lastDay <= 0 && memberLaunchProjectVO.getPercentage() < 100) {
                    memberLaunchProjectVO.setStatus(3);
                }


                switch (memberLaunchProjectVO.getStatus()) {
                    case 0:
                        memberLaunchProjectVO.setStatusText("审核中");
                        break;
                    case 1:
                        memberLaunchProjectVO.setStatusText("众筹中");
                        break;
                    case 2:
                        memberLaunchProjectVO.setStatusText("众筹成功");
                        break;
                    case 3:
                        memberLaunchProjectVO.setStatusText("众筹失败");
                        break;
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }

//            将百分比精确到后两位小数
            BigDecimal b = new BigDecimal(memberLaunchProjectVO.getPercentage());
            double percent = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            memberLaunchProjectVO.setPercentage(percent);
        });
        return memberLauchInfoVOList;
    }
}
