package com.ecnu.zz.core;


import com.ecnu.zz.msg.simplemsg.ResponseMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 2018/3/26 12:28
 * @desc :
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.addAvailStorage("192.168.0.100");
        responseMsg.addAvailStorage("192.168.0.110");
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
        System.out.println("client: " + msg);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
