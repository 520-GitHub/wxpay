package com.example.wxpaydemo.pojo;

public class WeChatConstant {
    public final static String MCH_ID = "1607856885";                   // mch_id
    public final static String MCH_KEY = "89jy89jy89jy89jy89jy89jy89jy89jy";                        // MCH_KEY
    public final static String APPID = "wx9429ee1b66e5e877";                  // APPID
    public final static String SECRET = "84875de2b1df04ba2e569ebb70843424";                   // SECRET
    public final static String TRADETYPE = "NATIVE";                   // SECRET

    public final static String urlStr = "https://api.mch.weixin.qq.com/pay/unifiedorder"; // 固定 请求
    public final static String notifyUrl = "http://cswe2n.natappfree.cc/wechat/report"; // 微信支付成功后回调地址
    public final static String urlStrGet = "https://api.mch.weixin.qq.com/pay/orderquery"; // 固定 查询

}
