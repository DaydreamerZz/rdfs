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

//    private ArrayList<String> agentLogs = new ArrayList<>();
    private static LinkedHashSet<String> agentLogs = new LinkedHashSet();
    public static LinkedHashSet<String> getAgentLogs(){
        return agentLogs;
    }

    public void addAgentLogs(ArrayList<String> logs){
        for(String log : logs){
            /*if(!agentLogs.contains(log)){
                agentLogs.add(log);
            }*/
            agentLogs.add(log);
        }

        System.out.println("AgentLogUtil.addAgentLogs: " + agentLogs);
    }

    public static void rebuildAgentLogs(){
        if(agentLogs.size() != 0)
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
                agentLogs.add(line);
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

        System.out.println("AgentLogUtil.rebuildAgentLogs rebuild agent's log structure: " + agentLogs);
    }

}
