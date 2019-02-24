package com.learning.search.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Validator {

    private static Map<String, String> PARAMS_MAP = null;

    static {
        PARAMS_MAP = new HashMap<>();
    }

    private final static String ERR_MSG = "的检索长度不能超过20汉字或字符;";

    public static Response<String> validate(Object input){
        Class<?> clss = input.getClass();
        Field[] declaredFields = clss.getDeclaredFields();
        StringBuffer buffer = new StringBuffer();
        int count = 0;
        try {
            for (Field field: declaredFields) {
                count ++;
                String fieldName = field.getName();
                if("serialVersionUID".equalsIgnoreCase(fieldName)){
                    continue;
                }
                boolean flag = field.isAccessible();
                field.setAccessible(true);
                Object o = field.get(input);

                String value = "";
                if(o instanceof String || o instanceof Integer || o instanceof Long) {
                    value = String.valueOf(o);
                }

                if (value.length() >= 20){
                    String separator = "/";
                    if(count == declaredFields.length -1) {
                        separator = "";
                    }
                    buffer.append(PARAMS_MAP.get(fieldName) + separator);
                }

                field.setAccessible(flag);
            }

            if(buffer.length() > 0){
                buffer.append(ERR_MSG);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return Response.errorMessage(buffer.toString());
    }
}
