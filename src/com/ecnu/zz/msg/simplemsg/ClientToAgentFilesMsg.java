package com.ecnu.zz.msg.simplemsg;

import java.io.Serializable;
import java.util.List;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 18-5-6 下午8:58
 * @desc :
 */
public class ClientToAgentFilesMsg implements Serializable{
    private static final long serialVersionUID = 1L;
    private String remoteRdmaAddress;
    private List<String> fileNames;

    public String getRemoteRdmaAddress() {
        return remoteRdmaAddress;
    }

    public void setRemoteRdmaAddress(String remoteRdmaAddress) {
        this.remoteRdmaAddress = remoteRdmaAddress;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    @Override
    public String toString() {
        return "ClientToAgentFilesMsg{" +
                "remoteRdmaAddress='" + remoteRdmaAddress + '\'' +
                ", fileNames=" + fileNames +
                '}';
    }
}
