package com.ecnu.zz.msg.simplemsg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 2018/4/27 13:02
 * @desc :
 */
public class ResponseMsg implements Serializable {

    /**
     * 默认序列ID
     */
    private static final long serialVersionUID = 1L;
    private List<String> availStorages = new ArrayList<>();

    public List<String> getAvailStorages() {
        return availStorages;
    }


    public void setAvailStorages(List<String> availStorages) {
        this.availStorages = availStorages;
    }

    @Override
    public String toString() {
        return "ResponseMsg{" +
                "availStorages=" + availStorages +
                '}';
    }

    public void addAvailStorage(String host){
        availStorages.add(host);
    }
}
