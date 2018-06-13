package msg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 18-5-6 下午8:58
 * @desc :
 */

/*
有三个变量:
commandStr : 客户端执行的指令,比如upload file, list , list 191.168.0.100/tmp
remoteRdmaAddress和filePaths都作用在upload命令上, 第一个是记录client文件存放在哪个server上, 第二个是所有传输的文件名. 所有文件在Agent上将被记录在
 */
public class ClientToAgentFilesMsg implements Serializable{
    private static final long serialVersionUID = 1L;
    private String commandStr; //客户端当前执行的指令
    private String remoteRdmaAddress; //远程客户端的地址
    private ArrayList<String> filePaths; //

    public String getRemoteRdmaAddress() {
        return remoteRdmaAddress;
    }
    public void setRemoteRdmaAddress(String remoteRdmaAddress) {
        this.remoteRdmaAddress = remoteRdmaAddress;
    }

    public ArrayList<String> getFilePaths() {
        return filePaths;
    }
    public void setFileNames(ArrayList<String> filePaths) {
        this.filePaths = filePaths;
    }

    public String getCommandStr() {
        return commandStr;
    }
    public void setCommandStr(String commandStr) {
        this.commandStr = commandStr;
    }

    @Override
    public String toString() {
        return "ClientToAgentFilesMsg{" +
                "commandStr='" + commandStr + '\'' +
                ", remoteRdmaAddress='" + remoteRdmaAddress + '\'' +
                ", filePaths=" + filePaths +
                '}';
    }
}
