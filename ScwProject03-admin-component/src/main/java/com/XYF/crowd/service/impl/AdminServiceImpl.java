package com.XYF.crowd.service.impl;

import com.XYF.crowd.constant.CrowdConstant;
import com.XYF.crowd.entity.Admin;
import com.XYF.crowd.entity.AdminExample;
import com.XYF.crowd.exception.LoginAcctAlreadyInUseException;
import com.XYF.crowd.exception.LoginFailedException;
import com.XYF.crowd.exception.UpdateLoginAcctAlreadyInUseException;
import com.XYF.crowd.mapper.AdminMapper;
import com.XYF.crowd.service.AdminService;
import com.XYF.crowd.util.MD5Util;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @username 熊一飞
 */

@Service
@SuppressWarnings("all")
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);



    @Override
    public Admin getAdminByLoginAcct(String loginAcct) {
        QueryWrapper<Admin> qw = new QueryWrapper<>();
        qw.eq("login_acct",loginAcct);
        return adminMapper.selectOne(qw);
    }

    @Override
    public boolean remove(Integer id, Integer localUserId) {

        System.out.println("进入到删除id页面");


        boolean flag = true;

//       不匹配就执行
        if (!ObjectUtils.nullSafeEquals(id, localUserId)) {
            int count = adminMapper.deleteByPrimaryKey(id);
            if (count != 1) {
                flag = false;
            }
        } else {
            return false;
        }

        return flag;
    }

    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Override
    public void editAdmin(Admin admin) {

        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            throw new UpdateLoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
        }
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {

//        先删除本来就有的roleId简化一些代码，防止重复添加影响效率,根据adminId删除inner_admin_role表的数据
        adminMapper.delInnerAdminId(adminId);

//        然后再进行插入操作,防止执行空插入操作，需要判断一下
        if (roleIdList != null && roleIdList.size() > 0) {
            adminMapper.insertAdminRoleRelation(adminId,roleIdList);

        }

    }



    public void saveAdmin(Admin admin) {
//        1.密码加密
//        admin.setUserPswd(MD5Util.getSpringMd5(admin.getUserPswd()));
//        使用security的盐值加密
              admin.setUserPswd(MD5Util.setSaltPasswordEncoder(admin.getUserPswd()));

//        生成创建时间
        Date date = new Date();
        SimpleDateFormat create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        admin.setCreateTime(create.format(date));

//        执行保存
        try {
            int count = adminMapper.insert(admin);
        } catch (Exception e) {

//            假如检测到异常类型是这个，说明是账户的唯一性出错了
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }


    }

    public List<Admin> getAll() {
//        为空就是查找全部的数据
        return adminMapper.selectByExample(new AdminExample());
    }


    @Override
    public Admin getAdminByLoginAcct(Admin admin) {


//        1,根据登录账号查询Admin对象

//        使用MP来查询对象
        QueryWrapper<Admin> qw = new QueryWrapper<>();
        qw.eq("login_acct", admin.getLoginAcct());
        List<Admin> adminList = adminMapper.selectList(qw);


//        2.判断Amdin对象是否为null
        if (adminList == null || adminList.size() == 0) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAiLIED);
        }


        if (adminList.size() > 1) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_SYSTEM_ERROR_NOT_UNIQUE);
        }

        Admin adminTwo = adminList.get(0);

//        3.如果Admin为null就抛出异常
        if (adminList == null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAiLIED);
        }


//        4.如果Admin对象不为null则将数据库密码从Admin对象中取出来

        String userPswdDB = admin.getUserPswd();

//        5.将表单提交的明文密码进行加密
//        String usepswdForm = MD5Util.md5SCW(userPswdDB);

//        使用security的盐值加密
        String usepswdForm = MD5Util.setSaltPasswordEncoder(userPswdDB);

//        6.对密码进行比较
//        使用的是Objects工具类，这里的先比的是，是否是同一个对象，然后再比较值
        if (!Objects.equals(usepswdForm, adminTwo.getUserPswd())) {
//        7.如果比较结果不一致则抛出异常
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAiLIED);
        }


//        8.如果一致则返回Admin对象
        return adminTwo;
    }

    /**
     * 分页方法
     *
     * @param keyword  查询的名字
     * @param pageNum  起始页
     * @param pageSize 每页显示的数量
     * @return
     */
    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {

//        调用PageHelper
        PageHelper.startPage(pageNum, pageSize);

//        执行查询
        List<Admin> admins = adminMapper.selectAdminByKeyword(keyword);


//        返回
        return new PageInfo<Admin>(admins);
    }


}
