package com.example.wxpaydemo.config;


import com.example.wxpay.sdk.IWXPayDomain;
import com.example.wxpay.sdk.WXPayConfig;
import com.example.wxpay.sdk.WXPayConstants;
import com.example.wxpaydemo.pojo.WeChatConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
public class MyWXPayConfig extends WXPayConfig {

    @Override
    public String getAppID() {
        return WeChatConstant.APPID;
    }

    @Override
    public String getMchID() {
        return WeChatConstant.MCH_ID;
    }

    @Override
    public String getKey() {
        return WeChatConstant.MCH_KEY;
    }

    @Override
    public InputStream getCertStream() {
        return null;
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {
                log.info("report");
            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
    }
}

