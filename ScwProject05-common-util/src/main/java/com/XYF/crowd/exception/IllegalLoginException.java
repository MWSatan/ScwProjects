package com.XYF.crowd.exception;

/**
 * @username 熊一飞
 */

//用户没有登录就访问资源抛出的异常
public class IllegalLoginException extends RuntimeException {
    public IllegalLoginException() {
    }

    public IllegalLoginException(String message) {
        super(message);
    }

    public IllegalLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalLoginException(Throwable cause) {
        super(cause);
    }

    public IllegalLoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
