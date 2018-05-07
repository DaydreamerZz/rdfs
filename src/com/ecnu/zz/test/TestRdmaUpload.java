package com.ecnu.zz.test;

import com.ecnu.zz.utils.RdmaUtil;

public class TestRdmaUpload {
    public static void main(String[] args){
        RdmaUtil.uploadFile("abc", "/tmp/files/");
    }

}
