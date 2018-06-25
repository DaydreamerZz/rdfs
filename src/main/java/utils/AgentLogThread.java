package utils;

import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 18-6-21 上午11:32
 * @desc :
 */
public class AgentLogThread implements Runnable {
    @Override
    public void run() {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
