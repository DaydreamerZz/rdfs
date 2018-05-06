package com.ecnu.zz.utils;

import java.io.*;
import java.util.ArrayList;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 2018/4/27 18:13
 * @desc :
 */
public class RdmaUtil {

    /*
    处理本地和远程文件路径,并且写入临时文件中
    @param fileOrDirPath 传输为文件或者目录路径
    @param remoteTargetDirPath 本地文件传输到服务端存放的地址
     */
    public static void tmpFileUpdate(String fileOrDirPath, String remoteTargetDirPath) {
        File rdmaTmpFileLocal = new File("/tmp/rdma_files_local");
        File rdmaTmpFileRemote = new File("/tmp/rdma_files_remote");
//        FileWriter fwLocal = null;
//        FileWriter fwRemote = null;
        BufferedWriter bwLocal = null;
        BufferedWriter bwRemote = null;

        try {
//            fwLocal = new FileWriter(rdmaTmpFileLocal);
//            fwRemote = new FileWriter(rdmaTmpFileRemote);
            bwLocal = new BufferedWriter(new FileWriter(rdmaTmpFileLocal, false));
            bwRemote = new BufferedWriter(new FileWriter(rdmaTmpFileRemote, false));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> localDirs = new ArrayList<>();
        ArrayList<String> localFiles = new ArrayList<>();

        ArrayList<String> remoteDirs = new ArrayList<>();
        ArrayList<String> remoteFiles = new ArrayList<>();

        FileUtil.traverseFolder(fileOrDirPath, remoteTargetDirPath, localDirs, localFiles, remoteDirs, remoteFiles); //得到所有本地文件路径,划分目录和文件

        try {
            if (localDirs.size() == 0) { //只有发送一个本地文件
                bwLocal.write(localFiles.get(0)+"\n");
                bwRemote.write(remoteFiles.get(0)+"\n");
                bwLocal.flush();
                bwRemote.flush();
            } else {
                int i, len;
                len = localDirs.size();
                for(i = 0; i < len; i++){
                    bwRemote.write(remoteDirs.get(i) + "\n");
                    bwLocal.write(localDirs.get(i) + "\n");
                }
                len = localFiles.size();
                for(i = 0; i < len; i++){
                    bwRemote.write(remoteFiles.get(i) + "\n");
                    bwLocal.write(localFiles.get(i) + "\n");
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


    }

    /*
    @param filePath 传输单个文件, 文件本地文件路径
     */
    public static void uploadFile(String filePath) {
        System.out.println("Rdma.uploadFile: " + filePath);
        InputStream in = null;
        try{
            //rdcppy -f file.txt -c /home/lab2/rdcp.cfg 传输单个文件
            //rdcppy -d -c /home/lab2/rdcp.cfg 传输目录

            Process process = Runtime.getRuntime().exec(new String[]{
                    "rdcpj", "-f", filePath, "-c", "/home/lab2/rdcp.cfg" //-c指定RDMA的配置文件
            });
            process.waitFor();
            in = process.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String result = br.readLine();
            System.out.println(result);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void uploadDir(String dirPath) {
        System.out.println("Rdma.uploadDir: " + dirPath);
        InputStream in = null;
        try{
            //rdcppy -c /home/lab2/rdcp.cfg
            //rdcppy file1.test.txt -b 409600 192.168.0.100:/home/lab1/files/

            Process process = Runtime.getRuntime().exec(new String[]{
                    "rdcpj", "-d", "-c", "/home/lab2/rdcp.cfg" //-c指定RDMA的配置文件
            });
            process.waitFor();
            in = process.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            char[] rdmaResult = new char[4096];
            br.read(rdmaResult);
            System.out.println(rdmaResult);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
