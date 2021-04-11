package com.example.wxpay.util;

import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class WeChatTest {

    public static void main(String[] args) {
        String urlStr = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String key = "123456789LANG123456789XIN1234567";
        SortedMap<Object, Object> sortedMap = new TreeMap<>();
        String order = "lx000001";
        sortedMap.put("appid","wxf55a6284e3569f46");
        sortedMap.put("mch_id","1553942691");
        sortedMap.put("nonce_str",order);
        sortedMap.put("body","继续教育");
        sortedMap.put("out_trade_no", order);
        sortedMap.put("total_fee", "1");
        sortedMap.put("spbill_create_ip","192.168.2.208");
        sortedMap.put("notify_url","http://139.9.169.17:8080/beiguokuai/wechat/report");
        sortedMap.put("trade_type","NATIVE");
        sortedMap.put("product_id","1");
        String sign = PayForUtil.createSign("UTF-8",sortedMap,key);
        sortedMap.put("sign",sign);
        String requestXml = PayForUtil.getRequestXml(sortedMap);
        String resxml = HttpUtil.postData(urlStr, requestXml);

        try {
            System.out.println(resxml);
            Map map = XMLUtil.doXMLParse(resxml);
            String result_code = (String) map.get("result_code");
            if (result_code !=null && result_code.equals("FAIL")){
                System.out.println(result_code);
            }
            String urlCode = (String) map.get("code_url");
            System.out.println(urlCode);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
