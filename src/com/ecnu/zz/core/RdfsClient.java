package com.ecnu.zz.core;

import com.ecnu.zz.msg.simplemsg.ClientToAgentFilesMsg;
import com.ecnu.zz.utils.CommandUtil;
import com.ecnu.zz.utils.RdmaUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 2018/3/25 19:17
 * @desc :
 */
public class RdfsClient {

    public static String remoteRdmaAddress = null;
    public static boolean DEBUG_RDMA_RUN = false;

    public static String getRemoteRdmaAddress() {
        return remoteRdmaAddress;
    }
    public static void setRemoteRdmaAddress(String remoteRdmaAddress) {
        RdfsClient.remoteRdmaAddress = remoteRdmaAddress;
    }

    public static boolean isDebugRdmaRun() {
        return DEBUG_RDMA_RUN;
    }

    public static void main(String[] args){
        try {
//            new RdfsClient().connect("127.0.0.1", 8080);
            new RdfsClient().connect("219.228.135.43", 8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect(String host, int port) throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder()); //in
                            socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder()); //out
//                            socketChannel.pipeline().addLast(new StringEncoder());
//                            socketChannel.pipeline().addLast(new StringDecoder());

                            socketChannel.pipeline().addLast(new ClientHandler()); //in
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();
            Channel channel = future.channel();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String command = null;

            /*
            client一直读取用户的输入,收到用户的输入后:
            1. 对输入进行检查,并提取出用户的操作类型,比如upload、download、delete
            2. 指令发送给server

             */
            while(true){
                command = in.readLine();
                int result = CommandUtil.parseStrCommand(command);
                if(result == CommandUtil.COMMAND_OK) {
//                    channel.writeAndFlush(command + "\n");
                    ArrayList<String> fileNames = RdmaUtil.getFileNames();
                    /*for(String fileName : fileNames){
//                        System.out.println("RdfsClient.connect " + fileName);
                        channel.write(fileName);
                    }
                    channel.flush();*/
                    ClientToAgentFilesMsg msg = new ClientToAgentFilesMsg();

                    //这里只能新建立一个ArrayList对象,否则数据还没发送到Agent,fileNames.clear()方法会把数据清空,Agent收到的数据就是空的
                    ArrayList<String> sendFileNames = new ArrayList<>();
                    for(String tmp : fileNames){
                        sendFileNames.add(tmp);
                    }
                    Collections.copy(sendFileNames, fileNames);
                    msg.setFileNames(sendFileNames);
                    msg.setRemoteRdmaAddress(remoteRdmaAddress);
                    channel.writeAndFlush(msg);
                    fileNames.clear();
                }else if(result == CommandUtil.COMMAND_NULL){
                    System.out.println("Command can not be empty");
                }else if(result == CommandUtil.COMMAND_UNSUPPORTED){
                    System.out.println("Command: " + command + " not supported");
                }else if(result == CommandUtil.COMMAND_UNKNOWN){
                    System.out.println("Command: " + command + " unknown");
                }else if(result == CommandUtil.COMMAND_ILLEGAL){
                    System.out.println("Command: " + command + " illegal");
                }

            }



        }finally {
//            future.channel().closeFuture().sync();
            group.shutdownGracefully();
        }
    }


}
