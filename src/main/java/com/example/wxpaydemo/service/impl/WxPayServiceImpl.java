package com.example.wxpaydemo.service.impl;

import com.example.wxpay.sdk.WXPay;
import com.example.wxpay.sdk.WXPayConfig;
import com.example.wxpay.sdk.WXPayConstants;
import com.example.wxpay.sdk.WXPayUtil;
import com.example.wxpay.util.HttpUtil;
import com.example.wxpay.util.PayForUtil;
import com.example.wxpay.util.XMLUtil;
import com.example.wxpaydemo.config.MyWXPayConfig;
import com.example.wxpaydemo.exception.WXPayPlaceOrderException;
import com.example.wxpaydemo.pojo.WeChatConstant;
import com.example.wxpaydemo.service.IPayRecordService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WxPayServiceImpl implements IPayRecordService {
    private static Logger log = Logger.getLogger(WxPayServiceImpl.class);


    @Override
    public String wxPayPC(String ipAddress) throws Exception {
        SortedMap<Object, Object> sortedMap = new TreeMap<>();
        sortedMap.put("appid", WeChatConstant.APPID);
        sortedMap.put("mch_id", WeChatConstant.MCH_ID);
        sortedMap.put("notify_url", WeChatConstant.notifyUrl);//回调地址
        sortedMap.put("trade_type", WeChatConstant.TRADETYPE);//类型
        //订单号  传入本地
        sortedMap.put("nonce_str", UUID.randomUUID().toString().replaceAll("-", ""));
        sortedMap.put("out_trade_no", UUID.randomUUID().toString().replaceAll("-", ""));//随机数//微信端需要，每次都不一样
        sortedMap.put("body", "支付标题");//支付内容
        sortedMap.put("total_fee", "1"); //价格  1 ==>0.01元
        sortedMap.put("spbill_create_ip", ipAddress);//获取ip地址
        sortedMap.put("product_id", "1"); //不清楚--待了解
        String sign = PayForUtil.createSign("UTF-8", sortedMap, WeChatConstant.MCH_KEY);
        sortedMap.put("sign", sign);
        String requestXml = PayForUtil.getRequestXml(sortedMap);
        String resxml = HttpUtil.postData(WeChatConstant.urlStr, requestXml);
        Map map = XMLUtil.doXMLParse(resxml);
        String resultCode = (String) map.get("result_code");
        if (resultCode != null && resultCode.equals("FAIL")) {
            String errCodeDes = (String) map.get("err_code_des");
            throw new WXPayPlaceOrderException(errCodeDes);
        }
        return (String) map.get("code_url");
    }


    @Override
    public Map<String, String> wxPayXCX(String openId, String periodGuid, String ipAddress)
            throws Exception {
        /**
         * 数据库获取订单
         */
        //orderId根据订单id 查询商品信息
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("body", "课程购买");           // 商家名称-销售商品类⽬、String(128)
        paraMap.put("openid", openId);            // openId，通过登录获取
        paraMap.put("total_fee", "1");           // ⽀付⾦额，单位分，即0.01元
        paraMap.put("out_trade_no", UUID.randomUUID().toString().replaceAll("-", ""));// 订单号
        paraMap.put("spbill_create_ip", ipAddress);
        paraMap.put("trade_type", "JSAPI");                 // ⽀付类型
//        paraMap.put("notify_url", urlFind+ "/personRegister/notify");
        log.info("paraMap为：" + paraMap);
        log.info("================ 分隔线 ==============");
        String out_trade_no = paraMap.get("out_trade_no");
        // 2. 通过MyWXPayConfig创建WXPay对象，⽤于⽀付请求
        final String SUCCESS_NOTIFY = "http://andyzheng.utools.club/wxPay/success";
        boolean useSandbox = false; // 是否使⽤沙盒环境⽀付API，是的话不会真正扣钱
        WXPayConfig config = new MyWXPayConfig();
        WXPay wxPay = new WXPay(config, SUCCESS_NOTIFY, false, useSandbox);

        // 3. fillRequestData会将上述参数⽤key=value形式和mchKey⼀起加密为sign，
        // 验证地址：  https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=20_1
        Map<String, String> map = wxPay.unifiedOrder(wxPay.fillRequestData(paraMap), 15000, 15000);

        // 4. 发送post请求"统⼀下单接⼝"(https://api.mch.weixin.qq.com/pay/unifiedorder), 返回预⽀付id:prepay_id
        String prepayId = (String) map.get("prepay_id");

        log.info("xmlStr为：" + map);
        log.info("================ 分隔线 ==============");

        Map<String, String> payMap = new HashMap<String, String>();
        payMap.put("appId", WeChatConstant.APPID);
        payMap.put("timeStamp", WXPayUtil.getCurrentTimestamp() + "");
        payMap.put("nonceStr", WXPayUtil.generateNonceStr());
        if (useSandbox) {
            payMap.put("signType", WXPayConstants.MD5);
        } else {
            payMap.put("signType", WXPayConstants.HMACSHA256);
        }
        payMap.put("package", "prepay_id=" + prepayId);
//        payMap.put("out_trade_no",out_trade_no);

        // 5. 通过appId, timeStamp, nonceStr, signType, package及商户密钥
        // 进⾏key=value形式拼接并加密
        String paySign = null;
        if (useSandbox) {
            paySign = WXPayUtil.generateSignature(payMap, WeChatConstant.MCH_KEY, WXPayConstants.SignType.MD5);
        } else {
            paySign = WXPayUtil.generateSignature(payMap, WeChatConstant.MCH_KEY,
                    WXPayConstants.SignType.HMACSHA256);
        }
        payMap.put("paySign", paySign);
        // 6. 将这个6个参数传给前端
        log.info("payMap：" + payMap);
        return payMap;
    }


}
