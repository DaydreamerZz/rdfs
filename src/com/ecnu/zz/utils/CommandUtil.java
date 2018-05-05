package com.ecnu.zz.utils;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 2018/4/27 18:00
 * @desc :
 */
public class CommandUtil {

    public static final int COMMAND_NULL = 0;
    public static final int COMMAND_ILLEGAL = 1;
    public static final int COMMAND_OK = 2;
    public static final int COMMAND_UNSUPPORTED = 3;
    public static final int COMMAND_UNKNOWN = 4;

    public static final String COMMAND_UPLOAD = "upload";
    public static final String COMMAND_DOWNLOAD = "download";
    public static final String COMMAND_DELETE = "delete";

    public static int parseStrCommand(String strCommand){

        if(strCommand == null || strCommand.length() == 0) //空指令，直接返回
            return COMMAND_NULL;

        String[] split = strCommand.split(" ");
        if(split.length < 2){ //非法指令,指令的参数个数不对
            return COMMAND_ILLEGAL;
        }else{
            switch (split[0].toLowerCase()){
                case COMMAND_UPLOAD:
                    int fileCheckResult = FileUtil.checkValidFilePath(split[1]);
                    if(fileCheckResult == FileUtil.IS_FILE){ //检查文件路径是否存在,并且可读,确保文件存在
                        RdmaUtil.uploadFile(split[1]);
                        return COMMAND_OK;
                    }else if(fileCheckResult == FileUtil.IS_DIR){ //检查文件路径是否存在,并且可读,确保目录存在
                        RdmaUtil.tmpFileUpdate(split[1]); //使用RDMA传输,需要读取/tmp目录下临时文件,所以每次先更新临时文件
                        RdmaUtil.uploadDir(split[1]);
                    }else
                        return COMMAND_ILLEGAL; //非法指令,指令参数有误,找不到文件

                case COMMAND_DOWNLOAD:

                    return COMMAND_UNSUPPORTED;
                case COMMAND_DELETE:

                    return COMMAND_UNSUPPORTED;
                default:
                    return COMMAND_UNKNOWN;
            }
        }


    }
}
