package test;


<<<<<<< HEAD
=======
import core.RdfsClient;
>>>>>>> 9843dba7e0f5e316e7f9ddbd6e635b055ee93e74
import core.RdfsConstants;
import utils.RdmaUtil;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 18-5-6 下午5:14
 * @desc :
 */
public class TestRdmaUtil {

    public static void main(String[] args) {
<<<<<<< HEAD
//        RdmaUtil.clientTmpFileUpdate("/home/lab2/files2/", RdfsConstants.BUFF_PATH);

//        RdmaUtil.uploadDir("/home/lab2/files2/");


        RdmaUtil.clientTmpFileUpdate("/home/lab2/a/files-10G-100", RdfsConstants.DISK_PATH);
//        RdmaUtil.clientTmpFileUpdate("/home/lab2/a/files-10G-10", RdfsConstants.DISK_PATH);

//        RdmaUtil.send("192.168.100.110", "/home/lab2/files/", "d1");
=======
//        RdmaUtil.clientTmpFileUpdate("/home/lab2/files2/", RdfsConstants.NVM_PATH);

//        RdmaUtil.uploadDir("/home/lab2/files2/");

        RdmaUtil.send("192.168.100.110", "/home/lab2/files/", "d1");
>>>>>>> 9843dba7e0f5e316e7f9ddbd6e635b055ee93e74

        return;
    }
}
