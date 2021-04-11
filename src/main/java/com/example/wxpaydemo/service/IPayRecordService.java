package com.example.wxpaydemo.service;

import java.util.Map;

public interface IPayRecordService {

    /**
     * Pc微信支付，获取支付二维码
     *
     * @param ipAddress
     */
    String wxPayPC(String ipAddress) throws Exception;

    /**
     * 微信小程序 支付
     */
    Map<String, String> wxPayXCX(String openId, String periodGuid, String ipAddress) throws Exception;


}
