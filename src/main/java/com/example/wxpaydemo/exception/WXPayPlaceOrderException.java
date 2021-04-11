package com.example.wxpaydemo.exception;


/**
 * 微信下单异常
 */
public class WXPayPlaceOrderException extends Exception {
    public WXPayPlaceOrderException() {
        super();
    }

    public WXPayPlaceOrderException(String message) {
        super(message);
    }

    public WXPayPlaceOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public WXPayPlaceOrderException(Throwable cause) {
        super(cause);
    }

    protected WXPayPlaceOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
