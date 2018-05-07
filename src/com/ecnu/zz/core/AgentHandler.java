package com.ecnu.zz.core;


import com.ecnu.zz.msg.simplemsg.ClientToAgentFilesMsg;
import com.ecnu.zz.msg.simplemsg.ResponseMsg;
import com.ecnu.zz.utils.FileUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

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
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq) msg;
        /*SubscribeReq req = (SubscribeReq) msg;

        if("tom".equalsIgnoreCase(req.getUserName())){
            System.out.println("Server accept client reqest: \n" + req.toString());

//            ctx.writeAndFlush(resp(req.getSubReqID()));
            ctx.writeAndFlush(resp(req.getSubReqID()));
        }*/
        ClientToAgentFilesMsg clientToAgentMsg = (ClientToAgentFilesMsg) msg;
        List<String> fileNames = clientToAgentMsg.getFileNames();
        String remoteRdmaAddress = clientToAgentMsg.getRemoteRdmaAddress();


//        FileUtil.buildLocalFileTree(fileNames); //Agent在本地建立文件信息,方便程序使用
//        FileUtil.logFileFromInfo(fileNames, remoteRdmaAddress); //在日志文件中记录每个文件的属于哪个服务器,也就是记录文件所在的ip地址


        System.out.println("client: " + msg);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
