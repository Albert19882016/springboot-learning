package com.learning.search.utils;

public class BizException extends RuntimeException {

    private String code;

    private String message;

    public BizException() {
    }

    public BizException(String code) {
        this(code, (String) null);
    }

    public BizException(String code, String message) {
        this(code, message, null);
    }

    public BizException(String code, Throwable cause) {
        this(code, null, cause);
    }

    public BizException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
