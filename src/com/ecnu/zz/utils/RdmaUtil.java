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

    public static void main(String[] args) {

        return;
    }

    /*
    @param filePath 传输单个文件, 文件本地文件路径
     */
    public static void uploadFile(String filePath) {
        System.out.println("Rdma.uploadFile: " + filePath);
        InputStream in = null;
        try{
            //rdcppy -c /home/lab2/rdcp.cfg
            //rdcppy file1.test.txt -b 409600 192.168.0.100:/home/lab1/files/

            Process process = Runtime.getRuntime().exec(new String[]{
                    "rdcppy", filePath, "-c", "/home/lab2/rdcp.cfg" //-c指定RDMA的配置文件
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

    public static void tmpFileUpdate(String fileOrDirPath) {
        File rdmaTmpFileLocal = new File("/tmp/rdma_files_local");
        File rdmaTmpFileRemote = new File("/tmp/rdma_files_remote");
        FileWriter fwLocal = null;
        FileWriter fwRemote = null;
        try {
            fwLocal = new FileWriter(rdmaTmpFileLocal);
            fwRemote = new FileWriter(rdmaTmpFileRemote);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> localDirs = new ArrayList<>();
        ArrayList<String> localFiles = new ArrayList<>();

        ArrayList<String> remoteDirs = new ArrayList<>();
        ArrayList<String> remoteFiles = new ArrayList<>();

        FileUtil.traverseFolder(fileOrDirPath, localDirs, localFiles, remoteDirs, remoteFiles); //得到所有本地文件路径,划分目录和文件
//        FileUtil.buildRemotePath(fileOrDirPath, localDirs, localFiles, remoteDirs, remoteFiles);
    }

    public static void uploadDir(String dirPath) {
        System.out.println("Rdma.uploadDir: " + dirPath);
        InputStream in = null;
        try{
            //rdcppy -c /home/lab2/rdcp.cfg
            //rdcppy file1.test.txt -b 409600 192.168.0.100:/home/lab1/files/

            Process process = Runtime.getRuntime().exec(new String[]{
                    "rdcppy", "-c", "/home/lab2/rdcp.cfg" //-c指定RDMA的配置文件
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
}
