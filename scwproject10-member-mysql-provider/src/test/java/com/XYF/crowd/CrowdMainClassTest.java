package com.XYF.crowd;

import com.XYF.crowd.entity.vo.*;
import com.XYF.crowd.mapper.MemberPOMapper;
import com.XYF.crowd.entity.po.MemberPO;
import com.XYF.crowd.mapper.OrderProjectMapper;
import com.XYF.crowd.mapper.ProjectPOMapper;
import com.XYF.crowd.util.MD5Util;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;

/**
 * @username 熊一飞
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@SuppressWarnings("all")
public class CrowdMainClassTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Autowired
    private OrderProjectMapper orderProjectMapper;

    private Logger logger = LoggerFactory.getLogger(CrowdMainClassTest.class);




    @Test
    public void testType() {
        List<PortalTypeVO> portalTypeVOList = projectPOMapper.selectPortalTypeVOList();

        portalTypeVOList.forEach(portalTypeVO -> {

            logger.info(portalTypeVO.getName() + "：" + portalTypeVO.getRemark());

            List<PortalProjectVO> portalProjectVOList = portalTypeVO.getPortalProjectVOList();
            portalProjectVOList.forEach(portalProjectVO -> {
                logger.info(portalProjectVO.toString());
            });
        });
    }

    @SneakyThrows
    @Test
    public void Test1() {
        List<MemberLaunchProjectVO> orderVOS = memberPOMapper.selectMemberProject(13);
        orderVOS.forEach(memberLaunchProjectVO -> {
            logger.info(memberLaunchProjectVO.toString());
        });
    }

    @Test
    public void Test2() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String source = "123456";

        MemberPO memberPO = new MemberPO(null, "dd", passwordEncoder.encode(source), " 杰 克 ",
                "jack@qq.com", 1, 1, "杰克", "123123", 2);
        memberPOMapper.insertSelective(memberPO);
    }

    @Test
    public void TestStringUtils() {
        String str = "";

        if (StringUtils.isEmpty(str)) {
            System.out.println("为空");
        }

        if (!StringUtils.isEmpty(str)) {
            System.out.println("不为空");
        }

    }

}
