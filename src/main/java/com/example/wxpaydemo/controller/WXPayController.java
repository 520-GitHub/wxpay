package com.example.wxpaydemo.controller;

import com.example.wxpay.sdk.WXPayUtil;
import com.example.wxpay.util.HttpUtil;
import com.example.wxpay.util.PayForUtil;
import com.example.wxpay.util.XMLUtil;
import com.example.wxpaydemo.pojo.WeChatConstant;
import com.example.wxpaydemo.pojo.WxPay;
import com.example.wxpaydemo.pojo.WxPayNotifyVO;
import com.example.wxpaydemo.service.IPayRecordService;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static java.util.Objects.isNull;

@Controller
public class WXPayController {
    @Resource
    private IPayRecordService payRecordService;

    @RequestMapping("/")
    public ModelAndView getWXUrl(ModelAndView modelAndView, HttpServletRequest request) {

        /**
         * 创建数据库订单等操作
         */
        String s = "";
        try {
            s = payRecordService.wxPayPC(getIpAddress(request));
        } catch (Exception e) {
            e.printStackTrace();
        }
        modelAndView.setViewName("index");
        modelAndView.addObject("url", s);
        modelAndView.addObject("money", "0.01元");
        return modelAndView;
    }

    /**
     * 获取当前用户的ip地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if (!isEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if (index != -1) {
                return XFor.substring(0, index);
            } else {
                return XFor;
            }
        }
        XFor = Xip;
        if (!isEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }

    public static boolean isEmpty(String str) {
        return isNull(str) || "".equals(str.trim());
    }

    /**
     *
     */
    /**
     * 页面发起订单查询
     */
    @ResponseBody
    @RequestMapping(value = "/orderQuery")
    public String orderQuery(String orderNum) {
        //查询本地订单状态
        String tip = "OK";
        /**
         * 查询数据库 是否完成该订单   //回调函数修改的数据库状态
         */
       /* PayRecord p = payRecordService.selectPayRecordByOrderId(orderNum);
        if ("1".equals(p.getIsPay())) {
            return "OK";
        }*/
        int num = 0;
        while (true) {
            Map<String, String> map = query(orderNum);
            if (map == null) {
                tip = "支付发生错误";
                break;
            }
            if ("SUCCESS".equals(map.get("trade_state"))) {
                //查询订单交易成功，处理本地db
                /**
                 *****
                 *****
                 **/
                tip = "OK";
                break;
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            num++;
            if (num >= 300) {
                tip = "支付超时";
                break;
            }
        }
        return tip;
    }

    /**
     * 查询微信端  是否完成订单
     */
    private Map<String, String> query(String orderNum) {
        //查询微信 支付订单状态
        SortedMap<Object, Object> sortedMap = new TreeMap<>();
        sortedMap.put("appid", WeChatConstant.APPID);
        sortedMap.put("mch_id", WeChatConstant.MCH_ID);
        sortedMap.put("out_trade_no", orderNum);//订单号
        sortedMap.put("nonce_str", WXPayUtil.generateNonceStr());  // 随机字符串
        String sign = PayForUtil.createSign("UTF-8", sortedMap, WeChatConstant.MCH_KEY);
        sortedMap.put("sign", sign);
        String requestXml = PayForUtil.getRequestXml(sortedMap);
        String resxml = HttpUtil.postData(WeChatConstant.urlStrGet, requestXml);
        Map<String, String> getMap = new HashMap<>();
        try {
            getMap = XMLUtil.doXMLParse(resxml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getMap;
    }


    /**
     * 手机微信 支付接口
     * ==============================================================================
     * 直接拉起支付功能
     */

    @ResponseBody
    @PostMapping("/pay")
    public Map<String, String> pay(HttpServletRequest request,
                                   @RequestBody WxPay wxPay) {
        // 获取请求ip地址
        String ipAddress = getIpAddress(request);
        Map<String, String> map = new HashMap<>();
        try {
            map = payRecordService.wxPayXCX(wxPay.getOpenId(), wxPay.getPeriodGuid(), ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("200", "出错");
        }
        return map;
    }


    /**
     * 接收微信完成订单的返回值  微信回调
     */
    @RequestMapping(value = "/success", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String success(HttpServletRequest request, @RequestBody WxPayNotifyVO param) {
        System.out.println("================================================开始处理微信小程序发送的异步通知");
        Map<String, String> result = new HashMap<String, String>();
        if ("SUCCESS".equals(param.getReturn_code())) {
            //处理db，数据库中的订单状态
            /**
             * ....处理db
             */
            //必须返回
            result.put("return_code", "SUCCESS");
            result.put("return_msg", "OK");
        }
        String s = "WXPayUtil.mapToXm处理出错";
        try {
            s = WXPayUtil.mapToXml(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }


}
