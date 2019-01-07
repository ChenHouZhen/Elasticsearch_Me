package com.chenhz.transportclientelasticsearch.utils;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

/**
 * @author CHZ
 * @create 2018/12/30
 */
public class Generator {

    @Autowired
    private Random random;

    public String generateRandomChars(String pattern , int length){
        return random.ints(0,pattern.length())
                .mapToObj(pattern::charAt)
                .limit(length)
                .collect(StringBuilder::new,StringBuilder::append,StringBuilder::append)
                .toString();
    }

    public int generateRandomInteger(int low ,int high){
        return random.ints(low,high)
                .findAny()
                .getAsInt();
    }

}
