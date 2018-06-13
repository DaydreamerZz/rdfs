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
//    private List<String> availStorages = new ArrayList<>();
    private String targetStorage;
    private ArrayList<String> listResults;

    public HashSet<String> getAgentMaintainDirTree() {
        return agentMaintainDirTree;
    }

    public void setAgentMaintainDirTree(HashSet<String> agentMaintainDirTree) {
        this.agentMaintainDirTree = agentMaintainDirTree;
    }

    private HashSet<String> agentMaintainDirTree;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    public ArrayList<String> getListResults() {
        return listResults;
    }

    public void setListResults(ArrayList<String> listResults) {
        this.listResults = listResults;
    }

    public String getTargetStorage() {
        return targetStorage;
    }

    public void setTargetStorage(String targetStorage) {
        this.targetStorage = targetStorage;
    }

    /*public List<String> getAvailStorages() {
        return availStorages;
    }
    public void setAvailStorages(List<String> availStorages) {
        this.availStorages = availStorages;
    }*/

    /*@Override
    public String toString() {
        return "ResponseMsg{" +
                "availStorages=" + availStorages +
                ", listResults=" + listResults +
                '}';
    }*/

//    public void addAvailStorage(String host){
//        availStorages.add(host);
//    }


    @Override
    public String toString() {
        return "AgentToClientMsg{" +
                "targetStorage='" + targetStorage + '\'' +
                ", listResults=" + listResults +
                ", agentMaintainDirTree=" + agentMaintainDirTree +
                '}';
    }
}
