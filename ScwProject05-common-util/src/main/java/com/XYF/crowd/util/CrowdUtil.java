package com.XYF.crowd.util;

import com.XYF.crowd.aliyun.api.gateway.demo.util.HttpUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @username 熊一飞
 * 尚筹网通用工具方法
 */


public class CrowdUtil {


    /**
     * 专门负责上传文件到 OSS 服务器的工具方法
     *
     * @param endPoint        OSS 参数
     * @param accessKeyId     OSS 参数
     * @param accessKeySecret OSS 参数
     * @param input           要上传的文件的输入流
     * @param bucketName      OSS 参数
     * @param bucketDomain    OSS 参数
     * @param originName      要上传的文件的原始文件名
     * @return 包含上传结果以及上传的文件在 OSS 上的访问路径
     */
    public static ResultEntity<String> uploadFileToOss(
            String endPoint,
            String bucketName,
            String accessKeyId,
            String accessKeySecret,
            String bucketDomain,
            InputStream input,
            String originName
    ) {

        // 创建 OSSClient 实例。
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
// 生成上传文件的目录
        String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());
// 生成上传文件在 OSS 服务器上保存时的文件名
// 原始文件名：beautfulgirl.jpg
// 生成文件名：wer234234efwer235346457dfswet346235.jpg
// 使用 UUID 生成文件主体名称
        String fileMainName = UUID.randomUUID().toString().replace("-", "");
// 从原始文件名中获取文件扩展名
        String extensionName = originName.substring(originName.lastIndexOf("."));
// 使用目录、文件主体名称、文件扩展名称拼接得到对象名称
        String objectName = folderName + "/" + fileMainName + extensionName;
        try {
// 调用 OSS 客户端对象的方法上传文件并获取响应结果数据
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName,
                    input);
// 从响应结果中获取具体响应消息
            ResponseMessage responseMessage = putObjectResult.getResponse();
// 根据响应状态码判断请求是否成功
            if (responseMessage == null) {
// 拼接访问刚刚上传的文件的路径
                String ossFileAccessPath = "http://"+bucketDomain + "/" + objectName;
// 当前方法返回成功
                return ResultEntity.successWithData(ossFileAccessPath);
            } else {
// 获取响应状态码
                int statusCode = responseMessage.getStatusCode();
                String errorMessage = responseMessage.getErrorResponseAsString();
// 当前方法返回失败
                return ResultEntity.failWithMessage(" 当 前 响 应 状 态 码 =" + statusCode + " 错 误 消 息 =" + errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
// 当前方法返回失败
            return ResultEntity.failWithMessage(e.getMessage());
        } finally {
            if (ossClient != null) {
// 关闭 OSSClient。
                ossClient.shutdown();
            }
        }

    }


    //    生成4位数字验证码
    public static String randomCode() {
        StringBuffer randomBuffer = new StringBuffer();
        for (int i = 0; i < 4; ++i) {
            int randomCode = (int) (Math.random() * 10);
            randomBuffer.append(randomCode);

        }
        return randomBuffer.toString();
    }


    /**
     * 验证码放到redis中
     *
     * @param customizeMethod 自定义请求方式
     * @param customizePath   自定义发送短信功能路径
     * @param phoneNum        手机号
     * @param appCode         短信接口的appCode
     * @param templateId      模板编号  默认为  TPL_0001
     * @param sign            签名编号  默认为   5
     * @return 成功返回验证码，失败返回错误信息
     * 状态码：200正常，400 请求参数错误，401 未授权或授权失败，403 次数用完，500 服务器内部错误，512 短信通道暂不可使用
     */
    public static ResultEntity<String> sendMessage(String customizeHost, String customizePath, String customizeMethod,
                                                   String phoneNum, String appCode,
                                                   String templateId, String sign) {

//短信接口调用地址
        String host = customizeHost;
//        String host = "https://dfsns.market.alicloudapi.com";

//        具体发送短信功能的地址
        String path = customizePath;

//        请求方式
        String method = customizeMethod;

//        登录到阿里云进入控制台找到已经购买的短信接口的AppCode

        String appcode = appCode;
        Map<String, String> headers = new HashMap<String, String>();

        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);

        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

//        封装其他函数
        Map<String, String> querys = new HashMap<String, String>();


        Map<String, String> bodys = new HashMap<String, String>();


//        将上方生成的验证码赋值到这里
        String code = randomCode();
//        模板中变量名与参数值，多项值以","分隔
        bodys.put("content", "code:" + code + ",expire_at:" + sign);

//        收短信的手机号
        bodys.put("phone_number", phoneNum);

//        测试模板ID
        bodys.put("template_id", templateId);


        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());

//            状态码：200正常，400 请求参数错误，401 未授权或授权失败，403 次数用完，500 服务器内部错误，512 短信通道暂不可使用
//            获取状态属性
            StatusLine statusLine = response.getStatusLine();
//            获取状态码
            int statusCode = statusLine.getStatusCode();
            String responsePhrase = statusLine.getReasonPhrase();
            if (statusCode == 200) {
//                返回验证码
                return ResultEntity.successWithData(code);
            }


            return ResultEntity.failWithMessage(responsePhrase);


        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failWithMessage(e.getMessage());
        }

    }

    /**
     * 判断当前请求是否为Ajax请求
     *
     * @param request 请求对象
     * @return true：是ajax请求
     * false: 不是ajax请求
     */
    public static boolean judgeRequestType(HttpServletRequest request) {

//        1.获取请求头信息
        String acceptFormation = request.getHeader("Accept");
        String xRequestHeader = request.getHeader("X-Requested-With");

//        判断
        if (
                (acceptFormation != null && acceptFormation.length() > 0 && acceptFormation.contains("application/json"))
                        ||
                        (xRequestHeader != null && xRequestHeader.length() > 0 && xRequestHeader.equals("XMLHttpRequest"))
        ) {
            return true;
        }

        return false;
    }


}
