package test;

import utils.ServerFileCheck;
import utils.ServerFileThread;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 18-6-21 下午1:48
 * @desc :
 */
public class TestServerFileThread {
    public static void main(String[] args) {
        ServerFileCheck.traverseFolder();
        Thread thread = new Thread(new ServerFileThread());
        thread.start();
        return;
    }
}
