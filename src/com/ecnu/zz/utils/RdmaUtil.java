package com.ecnu.zz.utils;

import com.ecnu.zz.core.RdfsClient;

import java.io.*;
import java.util.ArrayList;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 2018/4/27 18:13
 * @desc :
 */
public class RdmaUtil {



    //这个列表用来记录每次发送的文件路径信息,用来在传输完成之后,RdfsClient将其中内容发送给RdmaAgent,所以这个列表每次都会重新初始化
    public static ArrayList<String> filePaths;
    public static ArrayList<String> getFilePaths() {
        return filePaths;
    }


//rdcppy -f file.txt -c /home/lab2/rdcp.cfg 传输单个文件
//rdcppy -d -c /home/lab2/rdcp.cfg 传输目录

    /*
    @param filePath 传输单个文件, 文件本地文件路径
     */
    /*public static void uploadFile(String filePath, String remoteTargetDir) {
        System.out.println("Rdma.uploadFile: " + filePath);
        //start 为了把发送文件信息记录在fileNames列表中,这里多出来的处理步骤.在发送目录中不需要,因为已经处理好了
            if(!remoteTargetDir.endsWith("/"))
                remoteTargetDir += "/";
        String substring = filePath.substring(filePath.lastIndexOf("/") + 1);

//        fileNames = new ArrayList<>();
        filePaths.add(remoteTargetDir + substring);
//        System.out.println("ok: " + fileNames.get(0));
        //end

        InputStream in = null;
        try{
            if(RdfsClient.isDebugRdmaRun()){
                System.out.println("use rdma send files");
                Process process = Runtime.getRuntime().exec(new String[]{
                        "rdcpj", "-f", filePath, "-c", "/home/lab2/rdcp.cfg" //-c指定RDMA的配置文件
                });
                process.waitFor();
                in = process.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                char[] rdmaResult = new char[1024];
                br.read(rdmaResult);
                System.out.println(rdmaResult);
            }else{
                System.out.println("RdmaUtil.uploadFile DEBUG: do not use rdma send files");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/



    /*
    处理本地和远程文件路径,并且写入临时文件中,在uploadDir()方法之前被调用
    @param fileOrDirPath 传输为文件或者目录路径
    @param remoteTargetDirPath 本地文件传输到服务端存放的地址
    @return 返回所有文件的大小MB
     */
    public static double tmpFileUpdate(String fileOrDirPath, String remoteTargetDirPath) {
        double totolSize = 0;
        filePaths = new ArrayList<>();

        File rdmaTmpFileLocal = new File("/tmp/rdma_files_local");
        File rdmaTmpFileRemote = new File("/tmp/rdma_files_remote");

        BufferedWriter bwLocal = null;
        BufferedWriter bwRemote = null;

        try {
            bwLocal = new BufferedWriter(new FileWriter(rdmaTmpFileLocal, false));
            bwRemote = new BufferedWriter(new FileWriter(rdmaTmpFileRemote, false));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> localDirs = new ArrayList<>();
        ArrayList<String> localFiles = new ArrayList<>();

        ArrayList<String> remoteDirs = new ArrayList<>();
        ArrayList<String> remoteFiles = new ArrayList<>();

        totolSize = FileUtil.traverseFolder(fileOrDirPath, remoteTargetDirPath, localDirs, localFiles, remoteDirs, remoteFiles);//得到所有本地文件路径,划分目录和文件

        //为了发送给Agent发送的文件列表
//        fileNames = new ArrayList<>();

        try {
            if (localDirs.size() == 0) { //只有发送一个本地文件
                bwLocal.write(localFiles.get(0)+"\n");
                bwRemote.write(remoteFiles.get(0)+"\n");
                filePaths.add(remoteFiles.get(0));
                bwLocal.flush();
                bwRemote.flush();
            } else {
                int i, len;
                len = localDirs.size();
                for(i = 0; i < len; i++){ //先把目录信息写入临时文件
                    bwRemote.write(remoteDirs.get(i) + "\n");
                    bwLocal.write(localDirs.get(i) + "\n");
                    filePaths.add(remoteDirs.get(i));
                }
                len = localFiles.size();
                for(i = 0; i < len; i++){ //文件写入临时文件
                    bwRemote.write(remoteFiles.get(i) + "\n");
                    bwLocal.write(localFiles.get(i) + "\n");
                    filePaths.add(remoteFiles.get(i));
                }
                bwLocal.flush();
                bwRemote.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bwLocal.close();
                bwRemote.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return totolSize;

    }

    public static void uploadDir(String dirPath) {
        System.out.println("Rdma.uploadDir: " + dirPath);
        InputStream in = null;
        try{
            //rdcppy -c /home/lab2/rdcp.cfg
            //rdcppy file1.test.txt -b 409600 192.168.0.100:/home/lab1/files/
            if(RdfsClient.isDebugRdmaRun()) {
                System.out.println("use rdma send files");
                /*Process process = Runtime.getRuntime().exec(new String[]{
                        "rdcpj", "-d", "-c", "/home/lab2/rdcp.cfg" //-c指定RDMA的配置文件
                });*/
                Process process = Runtime.getRuntime().exec(new String[]{
                        "rdcpj", "-d", "-b", "10485760", "-c", RdfsClient.getRemoteRdmaAddress() //-c指定RDMA的配置文件
                });

                process.waitFor();
                in = process.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                char[] rdmaResult = new char[4096];
                br.read(rdmaResult);
                System.out.println(rdmaResult);
            }else {
                System.out.println("RdmaUtil.uploadDir DEBUG: do not use rdma send files");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
