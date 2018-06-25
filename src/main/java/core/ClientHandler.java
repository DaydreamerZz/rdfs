package core;

//import Ch8Protobuf.SubscribeReqProto;
import msg.AgentToClientMsg;

import utils.FileUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 2018/3/26 12:51
 * @desc :
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof AgentToClientMsg) {
            AgentToClientMsg agentToClientMsg = (AgentToClientMsg) msg;
            if (agentToClientMsg.getListResults() == null) { //不是list指令查询结果
                if (RdfsClient.getRemoteRdmaAddress() == null) { //说明还未初始化,第一次从agent得到storage地址,目录树等信息
                    RdfsClient.setRemoteRdmaAddress(agentToClientMsg.getTargetStorage());
                    System.out.println("Chosen remote RDMA server address: " + RdfsClient.getRemoteRdmaAddress());
                }
                RdfsClient.setAgentDirTreeDup(agentToClientMsg.getAgentMaintainDirTree());
                System.out.println("Receive server response: " + agentToClientMsg);
//            System.out.println("current local dir tree: " + RdfsClient.getAgentDirTreeDup());
//                System.out.println("current local dir tree: " + FileUtil.formatDirTree(RdfsClient.getAgentDirTreeDup()));

            } else { //这里主要是list命令的返回结果
                System.out.println("Receive server response: " + agentToClientMsg.getListResults());
            }
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
