package com.ecnu.zz.core;


import com.ecnu.zz.msg.simplemsg.AgentToClientMsg;
import com.ecnu.zz.msg.simplemsg.ClientToAgentFilesMsg;
import com.ecnu.zz.msg.simplemsg.ResponseMsg;
import com.ecnu.zz.utils.AgentLogUtil;
import com.ecnu.zz.utils.FileUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 2018/3/26 12:28
 * @desc :
 */
public class AgentHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.addAvailStorage("192.168.0.100");
        responseMsg.addAvailStorage("192.168.0.100");
        ctx.writeAndFlush(responseMsg);

        //
        AgentLogUtil.rebuildAgentLogs();

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
        String remoteRdmaAddress = clientToAgentMsg.getRemoteRdmaAddress();

        System.out.println("AgentHandler.channelRead" + clientToAgentMsg);
//这个方式是在Agent完全重建目录树的结构,但是时候又没有必要
//        FileUtil.buildLocalFileTree(filePaths); //Agent在本地建立文件信息,方便程序使用

//        FileUtil.logFileWithFromAddress(filePaths, remoteRdmaAddress); //在日志文件中记录每个文件的属于哪个服务器,也就是记录文件所在的ip地址

        if(clientToAgentMsg.getCommandStr().startsWith("upload")){
            FileUtil.logFileSimple(filePaths);
        }else if(clientToAgentMsg.getCommandStr().startsWith("list")){
            //Agent执行list指令,返回结果给client
            ResponseMsg responseMsg = new ResponseMsg();


            String[] split = clientToAgentMsg.getCommandStr().split(" ");
            ArrayList<String> listResult = new ArrayList<>();
            Iterator<String> iterator = AgentLogUtil.getAgentLogs().iterator();
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
            responseMsg.setListResults(listResult);
            System.out.println("AgentHandler.channelRead 发送list结果给client: \n" + responseMsg);

            ctx.writeAndFlush(responseMsg);
        }


        System.out.println("AgentHandler.channelRead client: " + msg);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
