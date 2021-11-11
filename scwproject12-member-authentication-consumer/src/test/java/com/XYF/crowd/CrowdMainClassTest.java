package com.XYF.crowd;

import com.aliyun.api.gateway.demo.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @username 熊一飞
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrowdMainClassTest {

    @Test
    public void testDao() {

    }

    @Test
    public void testSendMessage() {

//短信接口调用地址
        String host = "https://dfsns.market.alicloudapi.com";

//        具体发送短信功能的地址
        String path = "/data/send_sms";

//        请求方式
        String method = "POST";

//        登录到阿里云进入控制台找到已经购买的短信接口的AppCode

        String appcode = "4ba1def73d53413182dda462ef199c80";
        Map<String, String> headers = new HashMap<String, String>();

        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);

        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

//        封装其他函数
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();

//        模板中变量名与参数值，多项值以","分隔
        bodys.put("content", "code:1313,expire_at:5");

//        收短信的手机号
        bodys.put("phone_number", "19170217695");

//        测试模板ID
        bodys.put("template_id", "TPL_0001");


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

