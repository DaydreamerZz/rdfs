package com.ecnu.zz.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

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


    /*
    @param filePath
    用来判断
     */
    public static int checkValidFilePath(String filePath) {
        File file = new File(filePath);

        if(file.exists() && file.canRead()){
            if(file.isFile())
                return IS_FILE;
            else
                return IS_DIR;

        }else {
            return IS_NOT_FILE_OR_DIR;
        }

    }


    public static void traverseFolder(String fileOrDirPath, ArrayList<String> localDirs, ArrayList<String> localFiles, ArrayList<String> remoteDirs, ArrayList<String> remoteFiles) {
//        int fileNum = 0, folderNum = 0;
        File file = new File(fileOrDirPath);
        if(file.isFile()){
            localFiles.add(file.getAbsolutePath());
            return;
        }else {
            if(fileOrDirPath.endsWith("/")){
                fileOrDirPath = fileOrDirPath.substring(0, fileOrDirPath.length()-1);
            }
            localDirs.add(fileOrDirPath);
            LinkedList<File> list = new LinkedList<File>();
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
//                    System.out.println("文件夹:" + f.getAbsolutePath());
//                    folderNum++;
                    localDirs.add(f.getAbsolutePath());
                    list.add(f);
                } else {
//                    System.out.println("文件:" + f.getAbsolutePath());
//                    fileNum++;
                    localFiles.add(f.getAbsolutePath());
                }
            }
            File tmpFile;
            while (!list.isEmpty()) {
                tmpFile = list.removeFirst();
                files = tmpFile.listFiles();
                for (File f2 : files) {
                    if (f2.isDirectory()) {
//                        System.out.println("文件夹:" + file2.getAbsolutePath());
//                        folderNum++;
                        localDirs.add(f2.getAbsolutePath());
                        list.add(f2);
                    } else {
                        localFiles.add(f2.getAbsolutePath());
//                        System.out.println("文件:" + f2.getAbsolutePath());
//                        fileNum++;
                    }
                }
            }
        }
//        System.out.println("文件夹共有:" + folderNum + ",文件共有:" + fileNum);

    }

    public static void buildRemotePath(String base, ArrayList<String> localDirs, ArrayList<String> localFiles, ArrayList<String> remoteDirs, ArrayList<String> remoteFiles) {
        if(localDirs.size() == 0){ //说明只传输一个文件

        }

        for(String dirPath : localDirs){

        }

        for(String filePath : localFiles){

        }
    }
}
