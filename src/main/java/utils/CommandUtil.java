package utils;

import core.RdfsClient;

import static core.RdfsConstants.*;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 2018/4/27 18:00
 * @desc :
 */
public class CommandUtil {



//    public static final String REMOTE_TARGET_DIR = "/mnt/nvm/";

    public static int parseStrCommand(String strCommand){

        if(strCommand == null || strCommand.length() == 0) //空指令，直接返回
            return COMMAND_NULL;

        long start;
        double totalSize;

        String[] split = strCommand.split(" ");
        if(split[0].toLowerCase().equals(COMMAND_LIST) && split.length <= 2){
            return COMMAND_LIST_OK;
        } else{
            switch (split[0].toLowerCase()){
                case COMMAND_PUT:
                    int fileCheckResult = FileUtil.checkValidFilePath(split[1]);
                    if(fileCheckResult == FileUtil.IS_FILE){ //检查文件路径是否存在,并且可读,确保文件存在
                        System.out.println("CommandUtil.parseStrCommand: send one file");
//                        RdmaUtil.uploadFile(split[1], RdfsClient.getRemoteRdmaDirectory());
                        totalSize = RdmaUtil.tmpFileUpdate(split[1], RdfsClient.getRemoteRdmaDirectory());//使用RDMA传输,需要读取/tmp目录下临时文件,所以每次先更新临时文件

                        start = TimeUtil.start();
                        RdmaUtil.uploadDir(split[1]);
                        System.out.printf("Total file size is %.2f MB, transmission time is %.2f s\n", totalSize, TimeUtil.getPeriod(start));
//                        System.out.println("file size: " + totalSize + "MB transmission time : " + TimeUtil.getPeriod(start) + "s") ;
                        return COMMAND_UPLOAD_OK;
                    }else if(fileCheckResult == FileUtil.IS_DIR){ //检查文件路径是否存在,并且可读,确保目录存在
                        System.out.println("CommandUtil.parseStrCommand: send one directory");
                        totalSize = RdmaUtil.tmpFileUpdate(split[1], RdfsClient.getRemoteRdmaDirectory()); //使用RDMA传输,需要读取/tmp目录下临时文件,所以每次先更新临时文件
                        start = TimeUtil.start();
                        RdmaUtil.uploadDir(split[1]);
                        System.out.printf("Total file size is %.2f MB, transmission time is %.2f s\n", totalSize, TimeUtil.getPeriod(start));
                        return COMMAND_UPLOAD_OK;
                    }else {
                        return COMMAND_ILLEGAL; //非法指令,指令参数有误,找不到文件
                    }
                case COMMAND_GET:
                    return COMMAND_UNSUPPORTED;
                case COMMAND_RM:
                    return COMMAND_DELETT_OK;
                default:
                    return COMMAND_UNKNOWN;
            }
        }


    }
}
