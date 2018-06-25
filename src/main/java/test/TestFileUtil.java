package test;

import utils.FileUtil;

import java.util.ArrayList;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 18-5-4 下午9:35
 * @desc :
 */
public class TestFileUtil {

    public static void main(String[] args) {
        FileUtil fileUtil = new FileUtil();

        testTraverseFolder(fileUtil);

//        testBuildLocalFileTree(fileUtil);

//        testLogFileSimple(fileUtil);


        /*int res = FileUtil.checkFilePath("/mnt/nvm/dir1");
        System.out.println(res);*/
        return;
    }

    public static void testTraverseFolder(FileUtil fileUtil){
        ArrayList<String> localDirs = new ArrayList<>();
        ArrayList<String> localFiles = new ArrayList<>();

        ArrayList<String> remoteDirs = new ArrayList<>();
        ArrayList<String> remoteFiles = new ArrayList<>();

//        fileUtil.traverseFolder("/home/lab2/files/", "/mnt/nvm", localDirs, localFiles, remoteDirs, remoteFiles);
        fileUtil.traverseFolder("/mnt/nvm/", "/home/lab2/files/", localDirs, localFiles, remoteDirs, remoteFiles);
//        fileUtil.traverseFolder("/home/lab2/files/1", "/home/lab1/", localDirs, localFiles, remoteDirs, remoteFiles);
        System.out.println(localDirs);
        System.out.println(localFiles);

        System.out.println(remoteDirs);
        System.out.println(remoteFiles);
    }

    /*public static void testBuildLocalFileTree(FileUtil fileUtil){
        ArrayList<String> filePaths = new ArrayList<>();

        filePaths.add("/mnt/nvm/");filePaths.add("/mnt/nvm/dir1/");
        filePaths.add("/mnt/nvm/dir1/dir11/");filePaths.add("/mnt/nvm/2");
        filePaths.add("/mnt/nvm/1");filePaths.add("/mnt/nvm/rdcp-script.sh");
        filePaths.add("/mnt/nvm/87M.xz");filePaths.add("/mnt/nvm/87M.xz");
        filePaths.add("/mnt/nvm/dir1/a");filePaths.add("/mnt/nvm/dir1/dir11/file2");



//        fileUtil.buildLocalFileTree(filePaths);
    }

    public static void testLogFileSimple(FileUtil fileUtil){
        ArrayList<String> filePaths = new ArrayList<>();

        filePaths.add("/mnt/nvm/");filePaths.add("/mnt/nvm/dir1/");
        filePaths.add("/mnt/nvm/dir1/dir11/");filePaths.add("/mnt/nvm/2");
        filePaths.add("/mnt/nvm/1");filePaths.add("/mnt/nvm/rdcp-script.sh");
        filePaths.add("/mnt/nvm/87M.xz");filePaths.add("/mnt/nvm/87M.xz");
        filePaths.add("/mnt/nvm/dir1/a");filePaths.add("/mnt/nvm/dir1/dir11/file2");

        fileUtil.logFileSimple(filePaths);
    }*/
}
