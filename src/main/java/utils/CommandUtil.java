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

        if("help".equalsIgnoreCase(strCommand)) {
            usage();
            return COMMAND_HELP_OK;
        }else if("exit".equalsIgnoreCase(strCommand)){
            return COMMAND_EXIT_OK;
        }

        if(strCommand == null || strCommand.length() == 0) //空指令，直接返回
            return COMMAND_NULL;

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
                        RdmaUtil.clientTmpFileUpdate(split[1], RdfsClient.getRemoteRdmaDirectory());//使用RDMA传输,需要读取/tmp目录下临时文件,所以每次先更新临时文件
                        RdmaUtil.uploadDir(split[1], RdfsClient.getRemoteRdmaAddress());

                        return COMMAND_UPLOAD_OK;
                    }else if(fileCheckResult == FileUtil.IS_DIR){ //检查文件路径是否存在,并且可读,确保目录存在
                        System.out.println("CommandUtil.parseStrCommand(): 开始传输目录" + split[1]);
                        RdmaUtil.clientTmpFileUpdate(split[1], RdfsClient.getRemoteRdmaDirectory()); //使用RDMA传输,需要读取/tmp目录下临时文件,所以每次先更新临时文件
<<<<<<< HEAD

                        //todo 拿到远程ip这里有问题
//                        RdmaUtil.uploadDir(split[1], RdfsClient.getRemoteRdmaAddress());
                        RdmaUtil.uploadDir(split[1], "192.168.0.100");
=======
                        RdmaUtil.uploadDir(split[1], RdfsClient.getRemoteRdmaAddress());
>>>>>>> 9843dba7e0f5e316e7f9ddbd6e635b055ee93e74

                        return COMMAND_UPLOAD_OK;
                    }else {
                        return COMMAND_ILLEGAL; //非法指令,指令参数有误,找不到文件
                    }
                case COMMAND_GET:
                    //get -f remote local
                    String tmp, remotePath, localPath;
                    boolean isF = false;
                    tmp = split[1];
                    if("-f".equalsIgnoreCase(tmp)){ //有-f选项,无需判断目录是否存在,但是还是需要确定是否有权限创建目录
                        if(split.length != 4)
                            return COMMAND_ILLEGAL;
                        remotePath = split[2];
                        localPath = split[3];
                        isF = true;
                    }else{
                        if(split.length != 3)
                            return COMMAND_ILLEGAL;
                        remotePath = split[1];
                        localPath = split[2];
                    }

                    int res = FileUtil.checkFilePath(localPath);//todo
                    if(isF){ // -f
                        if(FileUtil.IS_CREATE_FAIL == res){
                            System.out.println("无权限创建下载目录");
                            return COMMAND_ILLEGAL;
                        }
                    } else{
                        if(FileUtil.IS_EXIST == res){
                            System.out.println("目标目录已经存在, 使用-f选项可以覆盖当前已有目录");
                            return COMMAND_ILLEGAL;
                        } else if(FileUtil.IS_CREATE_FAIL == res){
                            System.out.println("无权限创建下载目录");
                            return COMMAND_ILLEGAL;
                        }
                    }

                    return COMMAND_GET_OK;
                case COMMAND_RM:
                    return COMMAND_DELETT_OK;
                default:
                    return COMMAND_UNKNOWN;
            }
        }
    }

//    @Test
    public static void usage() {
        System.out.println("RDFS: RDMA Distributed File System");
        System.out.println("Usage: command [generic options]");
        String usage = "\tput local_path remote_path\n" +
                "\tget remote_path localpath\n" +
                "\tlist path\n" +
                "\trm path\n";
        System.out.println(usage);
    }
}
