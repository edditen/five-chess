package com.tenchael.chess.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class BeanUtils {

    public static Map<String, Object> jsonToMap(String json) {
        return JSONObject.parseObject(json, Map.class);
    }

    public static <T> T jsonToObject(String json, Class<T> clazz) {
        return JSONObject.parseObject(json, clazz);
    }

    public static String objectToJson(Object object) {
        return JSON.toJSONString(object);
    }

    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        if (map == null) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(map, clazz);
    }


}
