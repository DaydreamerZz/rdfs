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


    /*
    @param fileOrDirPath 如果是文件路径,那么只会咋localFiles和remoteFiles添加相应的路径;如果是目录路径,会遍历目录下目录和文件,且目录本身也被添加到localDirs中.
     */
    public static void traverseFolder(String fileOrDirPath, String remoteTargetDirPath, ArrayList<String> localDirs, ArrayList<String> localFiles, ArrayList<String> remoteDirs, ArrayList<String> remoteFiles) {
//        int fileNum = 0, folderNum = 0;
        File file = new File(fileOrDirPath);
        if(file.isFile()){
            localFiles.add(file.getAbsolutePath());
        }else {
            if(fileOrDirPath.endsWith("/")){
                localDirs.add(fileOrDirPath);
                fileOrDirPath = fileOrDirPath.substring(0, fileOrDirPath.length()-1);
            }else{
                localDirs.add(fileOrDirPath + "/");
            }

            LinkedList<File> list = new LinkedList<File>();
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
//                    System.out.println("文件夹:" + f.getAbsolutePath());
//                    folderNum++;
                    localDirs.add(f.getAbsolutePath() + "/");
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
                        localDirs.add(f2.getAbsolutePath() + "/");
                        list.add(f2);
                    } else {
                        localFiles.add(f2.getAbsolutePath());
//                        System.out.println("文件:" + f2.getAbsolutePath());
//                        fileNum++;
                    }
                }
            }


        }

        if(!remoteTargetDirPath.endsWith("/")){
            remoteTargetDirPath += "/";
        }
        if(localDirs.size() == 0){ //说明只有一个文件,处理好文件路径就可以了
            String localPath = localFiles.get(0);
            int splitIndex = localPath.lastIndexOf('/')+1;
            /*String substring = localPath.substring(localPath.lastIndexOf('/')+1);
            remoteFiles.add(remoteTargetDirPath + substring);*/
            remoteFiles.add(parsePath(localPath, remoteTargetDirPath, splitIndex));
        }else{ //此时要对目录下所有文件和目录的地址进行转换
            String localPath = localDirs.get(0);
            int splitIndex = localPath.lastIndexOf('/')+1;
            for(String localDir : localDirs){
                remoteDirs.add(parsePath(localDir, remoteTargetDirPath, splitIndex));
            }

            for(String localFile : localFiles){
                remoteFiles.add(parsePath(localFile, remoteTargetDirPath, splitIndex));
            }
        }
//        System.out.println("文件夹共有:" + folderNum + ",文件共有:" + fileNum);



    }

    public static String parsePath(String localPath, String remoteTargetDirPath, int splitIndex){

        String substring = localPath.substring(splitIndex);
        return remoteTargetDirPath + substring;
    }
}
