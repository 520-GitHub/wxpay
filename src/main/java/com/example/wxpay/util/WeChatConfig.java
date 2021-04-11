package com.example.wxpay.util;

public class WeChatConfig {



    /**
     * 微信服务号APPID
     */
    public static String APPID= "wxf55a6284e3569f46";
    /**
     * 微信支付的商户号
     */
    public static String MCHID="1553942691";
    /**
     * 微信支付的API密钥
     */
    public static String APIKEY="123456789LANG123456789XIN1234567";
    /**
     * 微信支付成功之后的回调地址【注意：当前回调地址必须是公网能够访问的地址】
     */
    public static String WECHAT_NOTIFY_URL_PC="http://hgf949.natappfree.cc/wechat/report";
    /**
     * 微信下单API地址
     */
    public static String UFDODER_URL="https://api.mch.weixin.qq.com/pay/unifiedorder";
    /**
     * true为使用真实金额支付，false为使用测试金额支付（1分）
     */
    public static String WXPAY="false";

}

