package utils;

import core.RdfsAgent;
import core.RdfsConstants;
import core.RdfsServer;
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

        while (true){
            if(RdfsAgent.getSyncFlag()){
                AgentLogUtil.logSync();
                RdfsAgent.setSyncFlag(false);
            }
            try {
                System.out.println("Agent日志没有变化,无需同步");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
