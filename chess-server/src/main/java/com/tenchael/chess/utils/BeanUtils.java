package com.tenchael.chess.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class BeanUtils {

    public static Map<String, Object> jsonToMap(String json) {
        return JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
        });
    }

    public static String objectToJson(Object object) {
        return JSON.toJSONString(object);
    }

    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) {
        if (map == null) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Object obj = objectMapper.convertValue(map, beanClass);

        return obj;
    }

    public static Map<String, ?> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, ?> mappedObject = objectMapper.convertValue(obj, Map.class);
        return mappedObject;
    }

}
