package core;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 18-6-21 下午8:13
 * @desc :
 */
public class RdfsConstants {
    public static final String NVM_PATH = "/mnt/nvm/";
    public static final String NVM_BACKUP_PATH = "/mnt/backup/";

    public static final int NVM_PATH_LENGTH = NVM_PATH.length();


    public static final int COMMAND_NULL = 0;
    public static final int COMMAND_ILLEGAL = 1;
    public static final int COMMAND_OK = 2;
    public static final int COMMAND_UNSUPPORTED = 3;
    public static final int COMMAND_UNKNOWN = 4;
    public static final int COMMAND_UPLOAD_OK = 5;
    public static final int COMMAND_LIST_OK = 6;
    public static final int COMMAND_DELETT_OK = 7;
    public static final int COMMAND_GET_OK = 8;
    public static final int COMMAND_HELP_OK = 9;


    public static final String COMMAND_PUT = "put";
    public static final String COMMAND_GET = "get";
    public static final String COMMAND_RM = "rm";
    public static final String COMMAND_LIST = "list";

    public static final int _1MB = 1024 * 1024;


    public static final String RDMA_LOCAL = "/opt/rdfs/rdma_files_local";
    public static final String RDMA_REMOTE = "/opt/rdfs/rdma_files_remote";
    public static final String RDMA_DEBUG_LOCAL = "/opt/rdfs/server_local";
    public static final String RDMA_DEBUG_REMOTE = "/opt/rdfs/server_remote";
    public static final String RDMA_DIRTREE_LOG_FILE = "/opt/rdfs/rdma_dirtree_log_file";




    public static final String DEFAULT_DEBUG_IP = "192.168.100.110";
}
