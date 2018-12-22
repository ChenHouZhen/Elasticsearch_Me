package com.chenhz.transportclientelasticsearch.utils;

import java.util.UUID;

/**
 * @author CHZ
 * @create 2018/12/22
 */
public class UUIDGenerate {

    public static String create() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

}
