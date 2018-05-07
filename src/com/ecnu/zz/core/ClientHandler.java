package com.ecnu.zz.core;

//import Ch8Protobuf.SubscribeReqProto;
import com.ecnu.zz.msg.simplemsg.ResponseMsg;
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
        ResponseMsg responseMsg = (ResponseMsg) msg;
        if(responseMsg.getListResults() == null){ //不是list指令查询结果
            RdfsClient.setRemoteRdmaAddress(((ResponseMsg) msg).getAvailStorages().get(0));
            System.out.println("Receive server response: " + responseMsg);
            System.out.println("Chosen remote RDMA server address: " + RdfsClient.getRemoteRdmaAddress());
        }else {
            System.out.println("Receive server response: " + responseMsg.getListResults());
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
