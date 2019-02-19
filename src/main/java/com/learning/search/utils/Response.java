package com.learning.search.utils;

import org.apache.commons.lang3.StringUtils;
import java.io.Serializable;

public class Response<T> implements Serializable {

    private String code; // 返回代码
    private String message; // 返回消息
    private T data; // 返回内容

    Response(String code, String message, T data) {
        this.setCode(code);
        this.setMessage(message);
        this.setData(data);
    }

    public static <T> Response<T> create(String code, String message) {
        return new Response<T>(code, message, null);
    }

    public static <T> Response<T> create(String code, String message, T data) {
        return new Response<T>(code, message, data);
    }

    public static <T> Response<T> successData(String message, T data) {
        return create(CodeConstants.SUCCESS, message, data);
    }

    public static <T> Response<T> successMessage(String message) {
        return successData(message, null);
    }

    public static <T> Response<T> successData(T data) {
        return successData(null, data);
    }

    public static <T> Response<T> success() {
        return successMessage(null);
    }

    public static <T> Response<T> errorData(String message, T data) {
        return create(CodeConstants.ERROR, message, data);
    }

    public static <T> Response<T> error() {
        return errorMessage(null);
    }

    public static <T> Response<T> errorData(T data) {
        return errorData(null, data);
    }

    public static <T> Response<T> errorMessage(String message) {
        return errorData(message, null);
    }

    public static <T> Response<T> selectiveMessage(boolean success, String successMessage, String errorMessage) {
        if (success) {
            return Response.successMessage(successMessage);
        }
        return Response.errorMessage(errorMessage);
    }

    public static <T> Response<T> selectiveMessage(boolean success, String successMessage, String errorMessage, T data) {
        if (success) {
            return Response.successData(successMessage, data);
        }
        return Response.errorMessage(errorMessage);
    }

    public static <T> Response<T> error(Error e) {
        if (e == null) {
            return null;
        }
        return create(CodeConstants.ERROR, e.getMessage());
    }

    public static <T> Response<T> error(Exception e) {
        if (e == null) {
            return null;
        }
        return create(CodeConstants.ERROR, e.getMessage());
    }

    public static <T> Response<T> error(BizException e) {
        if (e == null) {
            return null;
        }
        String code = e.getCode();
        code = StringUtils.isEmpty(code) ? CodeConstants.ERROR : code;
        return create(e.getCode(), e.getMessage());
    }

    public Response() {
        this.setMessage("");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = StringUtils.isEmpty(message) ? "" : message;
    }
}
