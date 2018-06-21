package msg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author : Bruce Zhao
 * @email  : zhzh402@163.com
 * @date   : 2018/6/13 16:24
 * @desc   : Agent和Server之间的消息
 *
 */
public class AgentToServerMsg implements Serializable {

    /**
     * 默认序列ID
     */
    private static final long serialVersionUID = 1L;
    private String cmd;
    private String path;
    private String clientIp;
    private String clientDir;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getClientDir() {
        return clientDir;
    }

    public void setClientDir(String clientDir) {
        this.clientDir = clientDir;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "AgentToServerMsg{" +
                "cmd='" + cmd + '\'' +
                ", path='" + path + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", clientDir='" + clientDir + '\'' +
                '}';
    }
}
