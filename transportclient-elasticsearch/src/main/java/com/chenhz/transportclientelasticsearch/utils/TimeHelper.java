package com.chenhz.transportclientelasticsearch.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author CHZ
 * @create 2018/12/23
 */
public class TimeHelper {

    private static final String TIME_fORMAT = "yyyy-MM-dd HH:mm:ss";


    public static String dateToStr(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
