package com.XYF.crowd.api;

import com.XYF.crowd.entity.po.MemberPO;
import com.XYF.crowd.entity.vo.MemberLauchInfoVO;
import com.XYF.crowd.entity.vo.MemberLaunchProjectVO;
import com.XYF.crowd.entity.vo.MemberOrderVO;

import java.util.List;

/**
 * @username 熊一飞
 */
public interface MemberService {
    MemberPO getMemberPOByLoginAcct(String loginAcct);

    void saveMember(MemberPO memberPO);

    List<MemberOrderVO> getMemberOrderVOData(Integer memberId);

    void delOrderProject(Integer id);

     List<MemberLaunchProjectVO> getMemberProjectData(Integer memberId);
}
