package test;


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
//        RdmaUtil.clientTmpFileUpdate("/home/lab2/files2/", RdfsConstants.BUFF_PATH);

//        RdmaUtil.uploadDir("/home/lab2/files2/");


        RdmaUtil.clientTmpFileUpdate("/home/lab2/a/files-10G-100", RdfsConstants.DISK_PATH);
//        RdmaUtil.clientTmpFileUpdate("/home/lab2/a/files-10G-10", RdfsConstants.DISK_PATH);

//        RdmaUtil.send("192.168.100.110", "/home/lab2/files/", "d1");

        return;
    }
}
