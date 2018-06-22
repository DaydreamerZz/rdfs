package test;


import core.RdfsClient;
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
        RdmaUtil.tmpFileUpdate("/home/lab2/files2/", RdfsConstants.NVM_PATH);

        RdmaUtil.uploadDir("/home/lab2/files2/");

        //RdmaUtil.uploadFile("/home/lab2/files/1", "/tmp/files");
        return;
    }
}
