package com.ecnu.zz.utils;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 18-5-21 下午4:52
 * @desc :
 */
public class TimeUtil {

    public static long start(){
        return System.currentTimeMillis();
    }


    public static double getPeriod(long start){
        long period = System.currentTimeMillis() - start;
        return period / 1000.0;
    }
}
