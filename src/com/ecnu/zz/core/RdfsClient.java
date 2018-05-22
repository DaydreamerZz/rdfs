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
import java.util.HashSet;

/**
 * @author : Bruce Zhao
 * @email  : zhzh402@163.com
 * @date   : 2018/3/25 19:17
 * @desc   :
 */
public class RdfsClient {

    public static String remoteRdmaAddress = null;
    public static String remoteRdmaDirectory = "/mnt/nvm/";
    public static HashSet<String> agentDirTreeDup;


    //    public static String remoteRdmaDirectory = "/mnt/ext/";

    //DEBUG选项,false不使用rdma上传,true使用上传

    public static boolean DEBUG_RDMA_NOT_RUN = false; //在isDebugRdmaRun返回这个值的时候,不使用rdma发送,也就是假装发送
    public static boolean DEBUG_RDMA_RUN = true; //真的使用rdma发送

    /*
     * client对应的远程地址
     */
    public static String getRemoteRdmaAddress() {
        return remoteRdmaAddress;
    }
    public static void setRemoteRdmaAddress(String remoteRdmaAddress) {
        RdfsClient.remoteRdmaAddress = remoteRdmaAddress;
    }
    /*
     * client对应的远程目录
     */
    public static String getRemoteRdmaDirectory() {
        return remoteRdmaDirectory;
    }
    public static void setRemoteRdmaDirectory(String remoteRdmaDirectoryk) {
        RdfsClient.remoteRdmaDirectory = remoteRdmaDirectory;
    }

    /*
     * client接收到的server发送来的目录树
     */
    public static HashSet<String> getAgentDirTreeDup() {
        return agentDirTreeDup;
    }
    public static void setAgentDirTreeDup(HashSet<String> agentDirTreeDup) {
        RdfsClient.agentDirTreeDup = agentDirTreeDup;
    }

    public static boolean isDebugRdmaRun() {
        return DEBUG_RDMA_NOT_RUN;
//        return DEBUG_RDMA_RUN;
    }

    public static void main(String[] args){
        try {
            new RdfsClient().connect("127.0.0.1", 8080);
//            new RdfsClient().connect("219.228.135.215", 8080);
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
            ClientToAgentFilesMsg msg;
            while(true){
                command = in.readLine();
                int result = CommandUtil.parseStrCommand(command);
                if(result == CommandUtil.COMMAND_UPLOAD_OK) { //到这里,文件已经被传输到远程了
//                    channel.writeAndFlush(command + "\n");
                    ArrayList<String> fileNames = RdmaUtil.getFilePaths();
                    /*for(String fileName : fileNames){
//                        System.out.println("RdfsClient.connect " + fileName);
                        channel.write(fileName);
                    }
                    channel.flush();*/
                    msg = new ClientToAgentFilesMsg();
                    //这里只能新建立一个ArrayList对象,否则数据还没发送到Agent,fileNames.clear()方法会把数据清空,Agent收到的数据就是空的
                    ArrayList<String> sendFileNames = new ArrayList<>();
                    for(String tmp : fileNames){
                        sendFileNames.add(tmp);
                    }
//                    Collections.copy(sendFileNames, fileNames);
                    msg.setCommandStr(command);
                    msg.setFileNames(sendFileNames);
                    msg.setRemoteRdmaAddress(RdfsClient.getRemoteRdmaAddress());
                    System.out.println("RdfsClient in connect() before send: " + msg);

                    channel.writeAndFlush(msg);
                    fileNames.clear();
                }else if(result == CommandUtil.COMMAND_LIST_OK){
                    msg = new ClientToAgentFilesMsg();
                    msg.setCommandStr(command);
                    channel.writeAndFlush(msg);
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
