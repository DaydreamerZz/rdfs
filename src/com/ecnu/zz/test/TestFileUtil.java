package com.ecnu.zz.test;

import com.ecnu.zz.utils.FileUtil;

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


        ArrayList<String> localDirs = new ArrayList<>();
        ArrayList<String> localFiles = new ArrayList<>();

        ArrayList<String> remoteDirs = new ArrayList<>();
        ArrayList<String> remoteFiles = new ArrayList<>();

        fileUtil.traverseFolder("/home/lab2/files/", localDirs, localFiles, remoteDirs, remoteFiles);
        System.out.println(localDirs);
        System.out.println(localFiles);

        return;
    }
}
