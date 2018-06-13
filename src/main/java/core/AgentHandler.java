package core;


import msg.ClientToAgentFilesMsg;
import msg.AgentToClientMsg;
import utils.AgentLogUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.*;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 2018/3/26 12:28
 * @desc :
 */
public class AgentHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        String remoteAddress = incoming.remoteAddress().toString();
        remoteAddress = remoteAddress.substring(1, remoteAddress.indexOf(":"));
//        System.out.println(remoteAddress);
        int key = remoteAddress.hashCode() % 2;
//        System.out.println(key);

        AgentToClientMsg agentToClientMsg = new AgentToClientMsg();
        agentToClientMsg.setTargetStorage(Storage.get(key)); //根据客户端的ip地址选择了存放数据的服务器地址,返回给client

        /*HashSet<String> dirTree = new HashSet<>();
        Collections.copy(dirTree, AgentLogUtil.getAgentDirTree());*/
        agentToClientMsg.setAgentMaintainDirTree(AgentLogUtil.getAgentDirTree());

        ctx.writeAndFlush(agentToClientMsg);


    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /*SubscribeReq req = (SubscribeReq) msg;

        if("tom".equalsIgnoreCase(req.getUserName())){
            System.out.println("Server accept client reqest: \n" + req.toString());

//            ctx.writeAndFlush(resp(req.getSubReqID()));
            ctx.writeAndFlush(resp(req.getSubReqID()));
        }*/
        ClientToAgentFilesMsg clientToAgentMsg = (ClientToAgentFilesMsg) msg;
        ArrayList<String> filePaths = clientToAgentMsg.getFilePaths();
//        String remoteRdmaAddress = clientToAgentMsg.getRemoteRdmaAddress();

        System.out.println("AgentHandler.channelRead() " + clientToAgentMsg);
//这个方式是在Agent完全重建目录树的结构,但是时候又没有必要
//        FileUtil.buildLocalFileTree(filePaths); //Agent在本地建立文件信息,方便程序使用

//        FileUtil.logFileWithFromAddress(filePaths, remoteRdmaAddress); //在日志文件中记录每个文件的属于哪个服务器,也就是记录文件所在的ip地址
        String commandStr = clientToAgentMsg.getCommandStr();
        AgentToClientMsg agentToClientMsg = new AgentToClientMsg();

        if(commandStr.startsWith("upload")){
            AgentLogUtil.logFileSimple(filePaths);
            agentToClientMsg.setAgentMaintainDirTree(AgentLogUtil.getAgentDirTree());
        }else if(commandStr.startsWith("list")){
            //Agent执行list指令,返回结果给client

            String[] split = clientToAgentMsg.getCommandStr().split(" ");
            ArrayList<String> listResult = new ArrayList<>();
            Iterator<String> iterator = AgentLogUtil.getAgentDirTree().iterator();
            if(split.length == 1){ //只有一个list指令,那么返回一级目录就可以了
                listResult.add(iterator.next());
            }else{
                String listWhat = split[1];
                while(iterator.hasNext()){
                    String next = iterator.next();
                    if(next.startsWith(listWhat))
                        listResult.add(next);
                }
            }
            agentToClientMsg.setListResults(listResult);
            System.out.println("AgentHandler.channelRead 发送list结果给client: \n" + agentToClientMsg);

//            ctx.writeAndFlush(responseMsg);
        }else if(commandStr.startsWith("delete")){ //只要修改目录树信息就可以了,同时要记录日志
            String[] split = clientToAgentMsg.getCommandStr().split(" ");
            String removeTarget = split[1];

            LinkedHashSet<String> agentDirTree = AgentLogUtil.getAgentDirTree();
            ArrayList<String> deletedList = new ArrayList<>();
            Iterator<String> iterator = agentDirTree.iterator();
            String path;
            while(iterator.hasNext()){
                path = iterator.next();
                if(path.startsWith(removeTarget)){
                    iterator.remove();
                    deletedList.add(path);
                }
            }
            agentToClientMsg.setAgentMaintainDirTree(agentDirTree);
            AgentLogUtil.appendDeleteLog(deletedList);

            AgentLogUtil.rebuildDirTreeLog(agentDirTree);
        }

        ctx.writeAndFlush(agentToClientMsg);



        System.out.println("AgentHandler.channelRead client: " + msg);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
