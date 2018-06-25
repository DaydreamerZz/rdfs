package utils;

import core.RdfsConstants;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import static core.RdfsConstants.NVM_BACKUP_PATH;
import static core.RdfsConstants.NVM_LIMIT_SIZE;
import static core.RdfsConstants.NVM_PATH;


/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 18-6-21 上午11:33
 * @desc :
 */
public class ServerFileCheck {


    public static LinkedList<String> nvmFilesList = new LinkedList<>();

    /*
     * 该变量记录的是内存文件系统中文件的总大小
     */
    public static long totalSize = 0L;

    public static long getTotalSize() {
        return totalSize;
    }
    public static void setTotalSize(long totalSize) {
        ServerFileCheck.totalSize = totalSize;
    }

    /*
     * 该flag用于判断内存文件系统中的文件是否有改变,如果没有改变,通过查看该flag可以确定.每次server收到文件或者删除文件,需要修改该flag
     */
    public static boolean fileChangeFlag = false;

    public static boolean isFileChangeFlag() {
        return fileChangeFlag;
    }
    public static void setFileChangeFlag(boolean fileChangeFlag) {
        ServerFileCheck.fileChangeFlag = fileChangeFlag;
    }

    /*public ServerFileCheck(){
        nvmFilesList = new LinkedList<>();
    }*/

    public static void printNvmFilesList(){
        for(String path : nvmFilesList){
            System.out.println(path);
        }
    }


    public static long traverseFolder() {
        if(nvmFilesList.size() != 0){ //如果之前有数据,需要清空原始的数据
            nvmFilesList.clear();
        }
//        int fileNum = 0, folderNum = 0;
        File file = new File(NVM_PATH);
        if(file.isFile()){
            totalSize += file.length();
            nvmFilesList.add(file.getAbsolutePath());
        }else {
            LinkedList<File> list = new LinkedList<File>();
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    list.add(f);
                } else {
                    totalSize += f.length();
                }
                nvmFilesList.add(f.getAbsolutePath());
            }
            File tmpFile;
            while (!list.isEmpty()) {
                tmpFile = list.removeFirst();
                files = tmpFile.listFiles();
                for (File f2 : files) {
                    if (f2.isDirectory()) {
                        list.add(f2);
                    } else {
                        totalSize += f2.length();
                    }
                    nvmFilesList.add(f2.getAbsolutePath());
                }
            }


        }
        //        System.out.println("文件夹共有:" + folderNum + ",文件共有:" + fileNum);
//        return (totalSize*1.0) / _1MB;
        return totalSize;
    }

    /*
     * 当检查到NVM中文件总大小超过了设定值,写入部分文件到磁盘中去
     */
    public static void nvmWriteToDisk() {
        File file = new File(NVM_PATH);
        String srcFileName, targetFileName;
        File srcFile, targetFile;
        while(true){
            if(FileUtils.sizeOfDirectory(file) > NVM_LIMIT_SIZE){
                try {
                    srcFileName = nvmFilesList.getFirst();
                    nvmFilesList.removeFirst();
                    srcFile = new File(srcFileName);
                    String substring = srcFileName.substring(srcFileName.lastIndexOf("/") + 1);
                    targetFileName = NVM_BACKUP_PATH + substring;

                    targetFile = new File(targetFileName);
                    if(targetFile.exists()){ //如果备份文件存在了,删除之
                        targetFile.delete();
                    }
                    if(srcFile.isDirectory()) {
                        FileUtils.moveDirectory(srcFile, targetFile);
                    }else{
                        FileUtils.moveFile(srcFile, targetFile);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                break;
            }
        }

    }

    //todo
    //这里假设从Agent传过来的path是已经验证的,也就是,比如Client要求rm dir1,那么Agent要验证dir1是存在的,那么dir1也可能是目录或者是文件
    //目前Agent并没有做
    /*
     * type用来标记path是一个文件还是目录, true: 文件, false: 目录
     */
    public static void remove(String path) {
        System.out.println("ServerFileCheck.remove(): " + path);
        String realPath = NVM_PATH + path;
        File file;
        file = new File(realPath);
        if(!file.exists()){ //说明文件被移动了磁盘
            realPath = NVM_BACKUP_PATH + path;
            file = new File(realPath);
            if(!file.exists()){
                return;
            }
        }

        try {
            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);
            }else{
                FileUtils.deleteQuietly(file);
            }
        }catch (IOException e){
            e.printStackTrace();
        }


    }
}
