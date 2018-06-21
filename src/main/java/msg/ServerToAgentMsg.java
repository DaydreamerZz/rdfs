package msg;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author : Bruce Zhao
 * @email  : zhzh402@163.com
 * @date   : 18-6-13 下午5:20
 * @desc   : 存储服务器与Agent之间的消息
 */

public class ServerToAgentMsg implements Serializable{
    private static final long serialVersionUID = 1L;
    private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "ServerToAgentMsg{" +
                "ip='" + ip + '\'' +
                '}';
    }
}
