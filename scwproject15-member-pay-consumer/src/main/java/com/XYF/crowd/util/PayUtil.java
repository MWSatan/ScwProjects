package com.XYF.crowd.util;

import com.XYF.crowd.config.PayProperties;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @username 熊一飞
 */


public class PayUtil {


    /**
     * 为了调用支付宝，专门封装的方法
     * @param outTradeNo 外部的订单号，也就是商户的订单号，也就是我们生成的订单号
     * @param totalAmount 订单的总金额
     * @param subject 订单的标题，这里可以使用项目名称
     * @param body 商品的描述，这里可以使用回报描述
     * @param appId
     * @param merchantPrivateKey
     * @param alipayPublicKey
     * @param notifyUrl
     * @param returnUrl
     * @param signType
     * @param charset
     * @param gatewayUrl
     * @return 返回到页面上显示的支付宝登录页面
     */
    @SneakyThrows
    public static String sendRequestToAliPay(String outTradeNo,Double totalAmount,String subject,String body,String appId, String merchantPrivateKey, String alipayPublicKey, String notifyUrl, String returnUrl, String signType, String charset, String gatewayUrl) {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, merchantPrivateKey, "json", charset, alipayPublicKey, signType);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(returnUrl);
        alipayRequest.setNotifyUrl(notifyUrl);

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + outTradeNo + "\","
                + "\"total_amount\":\"" + totalAmount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();

       return result;
    }
}
