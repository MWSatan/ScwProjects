package com.XYF.crowd.util;

//security 安全包


import com.XYF.crowd.constant.CrowdConstant;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {


    public static String getMD5(String password) {

        if (password == null || password.length() == 0) {
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }

        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            // 标准的md5加密后的结果
            return buffer.toString();


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }



    //	使用spring自带的MD5加密工具
    public static String getSpringMd5(String password) {

        if (password == null || password.length() == 0) {
//			如果不是有效的字符串就抛出异常
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
        //			spring自带的md5加密工具
        return DigestUtils.md5DigestAsHex(password.getBytes()).toString();
    }



    //    尚硅谷使用的md5加密
    public static String md5SCW(String password) {
        if (password == null || password.length() == 0) {
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }

        try {
//				获取一个信息摘要器对象
            MessageDigest messageDigest = MessageDigest.getInstance("md5");

//				获取明文字符串对应的字节数组
            byte[] bytes = password.getBytes();

//				执行加密
            byte[] output = messageDigest.digest(bytes);

//            创建BigInteger对象
			/*
			sigNum
			0 : 0
			1 : 正数
		   -1 ：负数
			 */
            int sigNum = 1; //
            BigInteger bigInteger = new BigInteger(sigNum, output);

//			按照16进制将BigInteger对象转为16进制对象
            return bigInteger.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

//        出现异常也返回null
        return null;
    }


//    封装一个盐值加密
    public static String  setSaltPasswordEncoder(String password){
        if (password == null || password.length() == 0) {
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
        PasswordEncoder pwd = new BCryptPasswordEncoder();

        return pwd.encode(password);
    }


}
