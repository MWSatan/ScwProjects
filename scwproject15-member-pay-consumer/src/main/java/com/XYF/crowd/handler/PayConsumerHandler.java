package com.XYF.crowd.handler;

import com.XYF.crowd.api.MySqlRemoteService;
import com.XYF.crowd.config.PayProperties;
import com.XYF.crowd.constant.CrowdConstant;
import com.XYF.crowd.entity.po.MemberPO;
import com.XYF.crowd.entity.vo.MemberLoginVO;
import com.XYF.crowd.entity.vo.OrderProjectVO;
import com.XYF.crowd.entity.vo.OrderVO;
import com.XYF.crowd.util.PayUtil;
import com.XYF.crowd.util.ResultEntity;
import com.alipay.api.internal.util.AlipaySignature;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @username 熊一飞
 */

@Controller
public class PayConsumerHandler {

    @Autowired
    private PayProperties payProperties;

    @Autowired
    private MySqlRemoteService mySqlRemoteService;

    private Logger logger = LoggerFactory.getLogger(PayConsumerHandler.class);

    @SneakyThrows
    @RequestMapping("notify")
    public void notifyMethod(HttpServletRequest request) {

        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

//        验证签名是否正确
        boolean signVerified = AlipaySignature.rsaCheckV1(params, payProperties.getAlipayPublicKey(), payProperties.getCharset(), payProperties.getSignType()); //调用SDK验证签名

        //——请在这里编写您的程序（以下代码仅作参考）——

	/* 实际验证过程建议商户务必添加以下校验：
	1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
	3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
	4、验证app_id是否为该商户本身。
	*/
        if (signVerified) {//验证成功
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

            logger.info("out_trade_no=" + out_trade_no);
            logger.info("trade_no=" + trade_no);
            logger.info("trade_status=" + trade_status);


        } else {//验证失败

            //调试用，写文本函数记录程序运行情况是否正常
            //String sWord = AlipaySignature.getSignCheckContentV1(params);
            logger.info("验证失败");
        }
    }

    @SneakyThrows
    @RequestMapping("/return")
    public String returnMethod(HttpServletRequest request, HttpSession session) {

        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, payProperties.getAlipayPublicKey(), payProperties.getCharset(), payProperties.getSignType()); //调用SDK验证签名

        //——请在这里编写您的程序（以下代码仅作参考）——
        if (signVerified) {
            //商户订单号
            String orderNum = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //支付宝交易号
            String payOrderNum = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //付款金额
            String orderAmount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

            //保存到数据库
//          1.从session域中获取OrderVO对象
            OrderVO orderVO = (OrderVO) session.getAttribute(CrowdConstant.ATTR_NAME_ORDER_VO);
//            添加支付宝交易单号,其他已经在其他地方生成了
            orderVO.setPayOrderNum(payOrderNum);

//            取出当前用户id
            MemberLoginVO loginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_MEMBER);
            Integer memberId = loginVO.getId();
            orderVO.setMemberId(memberId);
            ResultEntity<String> orderPOResultEntity = mySqlRemoteService.saveOrderPORemote(orderVO);

            logger.info("Order save Result：" + orderPOResultEntity);


//            return "trade_no:" + payOrderNum + "<br/>out_trade_no:" + orderNum + "<br/>total_amount:" + orderAmount;
//            返回到众筹页面
            return "redirect:http://www.xyf2021.ltd/member/my/crowd/page.html";
        } else {

//            页面显示信息
            return "验证失败";

        }
    }

    /*
    这里必须加@ResponseBody 注解，让当前方法的返回值成为响应体
      ，在浏览器界面上显示支付宝支付界面
     */
    @RequestMapping("/generate/order")
    @ResponseBody
    public String getGenerateOrder(HttpSession session, OrderVO orderVO) {


//        1.取出orderProjectVO对象
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT);

//        2、将orderProjectVO组装至orderPO中
        orderVO.setOrderProjectVO(orderProjectVO);

//        3.生成订单号设置成orderPO

//        1) 根据当前日期时间生成字符串
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

//        2）使用UUID生成用户ID部份
        String userId = UUID.randomUUID().toString().replace("-", "").toUpperCase();

//        3）组装并设置为orderNum
        String orderNum = userId + date;
        orderVO.setOrderNum(orderNum);

//        4.计算总金额
        Double orderAmount = (double) (orderProjectVO.getSupportPrice() * orderProjectVO.getPurchase() + orderProjectVO.getFreight());
        orderVO.setOrderAmount(orderAmount);
        orderVO.setProjectId(orderProjectVO.getProjectId());

//        保存到session中
        session.setAttribute(CrowdConstant.ATTR_NAME_ORDER_VO, orderVO);

//       5. 调用专门的方法给支付宝接口发送请求
        String result = PayUtil.sendRequestToAliPay(
                orderNum,
                orderAmount,
                orderProjectVO.getProjectName(),
                orderProjectVO.getReturnContent(),
                payProperties.getAppId(),
                payProperties.getMerchantPrivateKey(),
                payProperties.getAlipayPublicKey(),
                payProperties.getNotifyUrl(),
                payProperties.getReturnUrl(),
                payProperties.getSignType(),
                payProperties.getCharset(),
                payProperties.getGatewayUrl()
        );
        return result;
    }

}
