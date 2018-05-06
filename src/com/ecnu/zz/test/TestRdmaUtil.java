package com.ecnu.zz.test;

import com.ecnu.zz.utils.RdmaUtil;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 18-5-6 下午5:14
 * @desc :
 */
public class TestRdmaUtil {

    public static void main(String[] args) {
        RdmaUtil.tmpFileUpdate("/home/lab2/files/", "/home/lab1/files/");

        RdmaUtil.uploadDir("/home/lab2/files/");
        return;
    }
}
