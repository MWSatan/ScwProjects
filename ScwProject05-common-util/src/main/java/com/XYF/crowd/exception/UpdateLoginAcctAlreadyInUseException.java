package com.XYF.crowd.exception;

/**
 * @username 熊一飞
 */

/**
 * 保存或更新时如果检测到登录账号重复抛出这个异常
 */
public class UpdateLoginAcctAlreadyInUseException extends  RuntimeException{
    public UpdateLoginAcctAlreadyInUseException() {
    }

    public UpdateLoginAcctAlreadyInUseException(String message) {
        super(message);
    }

    public UpdateLoginAcctAlreadyInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateLoginAcctAlreadyInUseException(Throwable cause) {
        super(cause);
    }

    public UpdateLoginAcctAlreadyInUseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
