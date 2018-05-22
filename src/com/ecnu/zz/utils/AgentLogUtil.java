package com.ecnu.zz.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 18-5-7 下午1:03
 * @desc :
 */
public class AgentLogUtil {

//    private ArrayList<String> agentDirTree = new ArrayList<>();
    private static LinkedHashSet<String> agentDirTree = new LinkedHashSet();
    public static LinkedHashSet<String> getAgentDirTree(){
        return agentDirTree;
    }

    public void addAgentDirTree(ArrayList<String> logs){
        for(String log : logs){
            /*if(!agentDirTree.contains(log)){
                agentDirTree.add(log);
            }*/
            agentDirTree.add(log);
        }

        System.out.println("AgentLogUtil.addAgentDirTree: " + agentDirTree);
    }

    public static void rebuildAgentDirTree(){
        if(agentDirTree.size() != 0)
            return;
        BufferedReader brLog = null;

        try {
            brLog = new BufferedReader(new FileReader("/tmp/rdma_log_file"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line;
        try {
            while ((line = brLog.readLine()) != null) {
                agentDirTree.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                brLog.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("AgentLogUtil.rebuildAgentDirTree rebuild agent's log structure: " + agentDirTree);
    }

}
