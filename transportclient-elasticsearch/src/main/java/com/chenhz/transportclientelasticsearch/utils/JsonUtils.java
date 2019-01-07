package com.chenhz.transportclientelasticsearch.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author CHZ
 * @create 2018/12/30
 */
public class JsonUtils {

    public static <T> String toJsonStr(T t){
        return JSON.toJSONString(t, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty);
    }
}
