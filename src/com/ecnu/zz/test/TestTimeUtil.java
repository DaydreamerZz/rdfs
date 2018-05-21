package com.ecnu.zz.test;

import com.ecnu.zz.utils.TimeUtil;

import java.io.File;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 18-5-21 下午5:09
 * @desc :
 */
public class TestTimeUtil {

    public static void main(String[] args) throws InterruptedException {
        long start = TimeUtil.start();
        Thread.sleep(100);
        System.out.println(TimeUtil.getPeriod(start));
        return;
    }
}
