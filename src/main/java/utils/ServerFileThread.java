package utils;

import org.apache.commons.io.FileUtils;

import java.io.File;

import static core.RdfsConstants.NVM_LIMIT_SIZE;
<<<<<<< HEAD
import static core.RdfsConstants.BUFF_PATH;
=======
import static core.RdfsConstants.NVM_PATH;
>>>>>>> 9843dba7e0f5e316e7f9ddbd6e635b055ee93e74

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 18-6-21 上午11:32
 * @desc :
 */
public class ServerFileThread implements Runnable {
    @Override
    public void run() {
<<<<<<< HEAD
        File file = new File(BUFF_PATH);
=======
        File file = new File(NVM_PATH);
>>>>>>> 9843dba7e0f5e316e7f9ddbd6e635b055ee93e74
        long previousSize = 0, currentSize;
        while (true) {
            currentSize = FileUtils.sizeOfDirectory(file);
            if (currentSize != previousSize) { //比价当前目录的大小和上一次目录的大小
                previousSize = currentSize;
                ServerFileCheck.traverseFolder();  //因为目录大小变化了,所以要重新遍历目录,从而得到最新的目录文件结构

                if (currentSize > NVM_LIMIT_SIZE) {
                    System.out.println("more than limit");
                    ServerFileCheck.nvmWriteToDisk();
                } else {
                    System.out.println("so much free space");
                }
//            System.out.println(currentSize);

            }else{
                System.out.println("no more changes");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }


    }
}
