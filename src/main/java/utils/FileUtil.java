package utils;


import core.RdfsConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 2018/4/27 18:08
 * @desc :
 */
public class FileUtil {

    public static final int IS_NOT_FILE_OR_DIR = 0;
    public static final int IS_FILE = 1;
    public static final int IS_DIR = 2;
    public static final int IS_EXIST = 3;
    public static final int IS_CREATE_SUCCUESS = 4;
    public static final int IS_CREATE_FAIL = 5;


    /*
    @param filePath
    用来判断指定路径的文件是否存在,
    返回值0:不存在 1:文件 2:目录
     */
    public static int checkValidFilePath(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.canRead()) {
            if (file.isFile())
                return IS_FILE;
            else
                return IS_DIR;

        } else {
            return IS_NOT_FILE_OR_DIR;
        }
    }


    /*
     * 判断用户指定的目录位置是否有访问权限
     */
    public static int checkFilePath(String localPath) {
        File file = new File(localPath);
        if(file.exists())
            return IS_EXIST;
        else{
            if(file.mkdir()){ //如果目录创建成功
                return IS_CREATE_SUCCUESS;
            } else{
                return IS_CREATE_FAIL;
            }
        }
    }

    /*
    @param fileOrDirPath 如果是文件路径,那么只会咋localFiles和remoteFiles添加相应的路径;如果是目录路径,会遍历目录下目录和文件,且目录本身也被添加到localDirs中.
    @return 返回所有文件的大小MB
    traverseFolder("/home/lab2/files", "/mnt/nvm", ...)
    traverseFolder("/mnt/nvm/f9", "/home/lab2/files/", ...)
     */
    public static long traverseFolder(String localPath, String remoteTargetDirPath,
                                        ArrayList<String> localDirs, ArrayList<String> localFiles,
                                        ArrayList<String> remoteDirs, ArrayList<String> remoteFiles) {
//        int fileNum = 0, folderNum = 0;
        long totalSize = 0;

        File file = new File(localPath);
        if (file.isFile()) {
            localFiles.add(file.getAbsolutePath());
//            totalSize += file.getTotalSpace();
            totalSize += file.length();
        } else {
            if (localPath.charAt(localPath.length() - 1) != '/') {
                localPath += '/';
            }
//增加的,为了让目录自己也发送过去
            localDirs.add(localPath);

            LinkedList<File> list = new LinkedList<File>();
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    localDirs.add(f.getAbsolutePath() + "/");
                    list.add(f);
                } else {
                    localFiles.add(f.getAbsolutePath());
                    totalSize += f.length();
                }
            }
            File tmpFile;
            while (!list.isEmpty()) {
                tmpFile = list.removeFirst();
                files = tmpFile.listFiles();
                for (File f2 : files) {
                    if (f2.isDirectory()) {
                        localDirs.add(f2.getAbsolutePath() + "/");
                        list.add(f2);
                    } else {
                        totalSize += f2.length();
                        localFiles.add(f2.getAbsolutePath());

                    }
                }
            }


        }

        if (!remoteTargetDirPath.endsWith("/")) {
            remoteTargetDirPath += "/";
        }

        if (file.isFile()) { //说明只有一个文件,处理好文件路径就可以了
//            String localPath = localFiles.get(0);
            int splitIndex = localPath.lastIndexOf('/') + 1;
            /*String substring = localPath.substring(localPath.lastIndexOf('/')+1);
            remoteFiles.add(remoteTargetDirPath + substring);*/
            remoteFiles.add(parsePath(localPath, remoteTargetDirPath, splitIndex));
        } else { //此时要对目录下所有文件和目录的地址进行转换
//            String localPath = localDirs.get(0);
            localPath = localPath.substring(0, localPath.length()-1);
            int splitIndex = localPath.lastIndexOf("/") + 1;
<<<<<<< HEAD
//            int splitIndex = RdfsConstants.BUFF_PATH.length(); //todo
=======
//            int splitIndex = RdfsConstants.NVM_PATH.length(); //todo
>>>>>>> 9843dba7e0f5e316e7f9ddbd6e635b055ee93e74
//            int splitIndex =

            for (String localDir : localDirs) {
                remoteDirs.add(parsePath(localDir, remoteTargetDirPath, splitIndex));
            }

            for (String localFile : localFiles) {
                remoteFiles.add(parsePath(localFile, remoteTargetDirPath, splitIndex));
            }
        }
//        System.out.println("文件夹共有:" + folderNum + ",文件共有:" + fileNum);
//        return (totalSize * 1.0) / _1MB;
        return totalSize;
    }

    /*
    @param: localPath 本地文件路径
    @param: remoteTargetDirPath 传输到服务器的目标目录
    @param: splitIndex 分割符所在的索引

    localPath: /home/lab2/files/dir1/1
    remoteTargetDirPath: /tmp/files/
    splitIndex: 18
    目的是拼接得到/tmp/files/dir1/1
     */
    public static String parsePath(String localPath, String remoteTargetDirPath, int splitIndex) {

        String substring = localPath.substring(splitIndex);
        return remoteTargetDirPath + substring;
    }


    /*
    @param: filePaths 文件被发送到Server
    被Agent调用
     *//*
    public static void buildLocalFileTree(ArrayList<String> filePaths) {
        String AGENT_AGGREGATION = null;
        if(RdfsClient.getRemoteRdmaAddress() == null){
            AGENT_AGGREGATION = "/tmp/192.168.0.100/";
        }else{
            AGENT_AGGREGATION = "/tmp/" + RdfsClient.getRemoteRdmaAddress() + "/";
        }
        boolean isDir = true;
        File dir = null;
        File file = null;
        for(String filePath : filePaths){
            try {
                if (isDir) {
                    if (!filePath.endsWith("/")) { //是普通文件
                        isDir = false;
                        file = new File(AGENT_AGGREGATION + filePath);
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                    } else { //确定是目录
                        dir = new File(AGENT_AGGREGATION + filePath);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                    }
                } else { //是普通文件
                    file = new File(AGENT_AGGREGATION + filePath);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/


    /*
    Agent收到client发送的消息(所有传输的文件路径信息)之后,记录到本地的log文件中,重复的不记录
     */
    public static void logFileSimple(ArrayList<String> filePaths) {
        File rdmaLogFile = new File("/tmp/rdma_log_file");
        if (!rdmaLogFile.exists()) {
            try {
                rdmaLogFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedWriter bwLog = null;
        try {
            bwLog = new BufferedWriter(new FileWriter(rdmaLogFile, true));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            LinkedHashSet<String> agentLogs = AgentLogUtil.getAgentDirTree();
            for (String filePath : filePaths) {
                if (!agentLogs.contains(filePath))
                    bwLog.write(filePath + "\n");
            }
            bwLog.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bwLog.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*//到这里, 存放到server的文件路径信息都写入了Agent自己的rdma_log_file中,但是为了效率的问题,同时写入一个内存结构,方便查找
        AgentLogUtil agentLogUtil = new AgentLogUtil();
        agentLogUtil.addAgentDirTree(filePaths);*/

    }

    public static String formatDirTree(HashSet<String> agentDirTreeDup) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        Iterator<String> iterator = agentDirTreeDup.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (next.endsWith("/")) {
                sb.append(next + "\n");
            }
        }
        return sb.toString();
    }


}
