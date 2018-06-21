package core;

//import Ch8Protobuf.SubscribeReqProto;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import msg.AgentToServerMsg;
import msg.ServerToAgentMsg;
import utils.RdmaUtil;
import utils.ServerFileCheck;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 2018/3/26 12:51
 * @desc :
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ServerToAgentMsg msg = new ServerToAgentMsg();
        msg.setIp("locahost");
        ctx.writeAndFlush(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if(msg instanceof  AgentToServerMsg) {
            AgentToServerMsg agentToServerMsg = (AgentToServerMsg) msg;
            String cmd = agentToServerMsg.getCmd();
            if("delete".equalsIgnoreCase(cmd)){
                String path = agentToServerMsg.getPath();
                ServerFileCheck.remove(path);

            }else if("get".equalsIgnoreCase(cmd)){
                String path = agentToServerMsg.getPath();
                String ip = agentToServerMsg.getClientIp();
                String dir = agentToServerMsg.getClientDir();
                RdmaUtil.send(ip, dir, path);
            }

            System.out.println(msg.toString());


        }else{
            System.out.println("Agent to Client msg Server received");
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
