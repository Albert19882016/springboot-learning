package com.learning.search.commons;

import com.learning.search.utils.ESConstants;
import java.util.HashMap;
import java.util.Map;

public class GetESTemplate {

    /**
     * search-template
     * 此例只提供模板，相关参数需要进行相应处理
     * 只筛选符合条件的结果，结果未经打分排序
     * @param inputMap
     * @return
     */
    public static Map<String,Object> searchTemplate(Map<String, String> inputMap) {
        Map<String,Object> resultMap = new HashMap<>();
        StringBuffer buffer = new StringBuffer();
        for (String key: inputMap.keySet()) {
            buffer.append(String.format(ESConstants.term_keyword, key, inputMap.get(key)));
            buffer.append(",");
        }
        buffer.append(String.format(ESConstants.range,"date",0,1));
        resultMap.put("filters", buffer.toString());
        resultMap.put("musts",String.format(ESConstants.match_all));
        resultMap.put("from",inputMap.get("from"));
        resultMap.put("size", inputMap.get("size"));
        return resultMap;
    }

}
