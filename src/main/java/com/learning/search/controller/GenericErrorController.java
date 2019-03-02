package com.learning.search.controller;

import com.learning.search.utils.ErrorEnum;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *  用户友好的错误说明（返回前台时展示的内容）
 *  第一种，直接自定义错误静态页面（/error）
 *
 * 第二种，自定义MyErrorController，继承BasicErrorController，
 * BasicErrorController在ErrorMvcAutoConfiguration中注册到spring中，
 * 自定义要返回前端的内容中要包含的参数，
 */
public class GenericErrorController extends BasicErrorController {

    public GenericErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
        super(errorAttributes, errorProperties);
    }

    public GenericErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorProperties, errorViewResolvers);
    }

    @Override
    protected Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        Map<String,Object> attrs = super.getErrorAttributes(request,includeStackTrace);
        /** 这里面只需要message和返回前端的判断码需要,其他删除就好
         * {
         *     "timestamp": "2018-08-02 13:02:30",
         *     "status": 500,
         *     "error": "Internal Server Error",
         *     "exception": "java.lang.IllegalArgumentException",
         *     "message": "编号不可为空",
         *     "path": "/manager/products"
         *     + code
         *     + canRetry
         * }
         */
        attrs.remove("timestamp");
        attrs.remove("error");
        attrs.remove("exception");
        attrs.remove("path");
        attrs.remove("status");
        //通过返回的message拿到错误对象
        String errorCode = (String) attrs.get("message");
        ErrorEnum errorEnum = ErrorEnum.getByCode(errorCode);
        //放入返回参数中
        attrs.put("message",errorEnum.getMessage());
        attrs.put("code",errorEnum.getCode());
        attrs.put("canRetry",errorEnum.isCanRetry());
        return attrs;
    }
}
