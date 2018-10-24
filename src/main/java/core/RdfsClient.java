package core;

import msg.ClientToAgentMsg;
import utils.CommandUtil;
import utils.RdmaUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

import static core.RdfsConstants.*;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 2018/3/25 19:17
 * @desc :
 */
public class RdfsClient {

    /*
     * Client upload文件时对应的远程IP地址,在和Agent建立连接的时候,Agent返回的消息中包含这个地址.
     */
    public static String remoteRdmaAddress = null;

    /*
     * Client upload文件时对应的远程目录.这个目录决定了是传输到内存文件系统,还是普通磁盘文件系统
     */

    public static String remoteRdmaDirectory = RdfsConstants.BUFF_PATH;

    /*
     * Agent会维护一个系统中所有文件的目录树结构,在Client进行了某些操作后,Agent会把最新的目录树给Client.
     * Client在upload文件的时候可以与目录树进行比较从而不上传已有的文件,也可以不管目录树直接上传
     */
    public static HashSet<String> agentDirTreeDup;

    /*
     * DEBUG选项,false不使用rdma上传,true使用上传
     */

    public static boolean DEBUG_RDMA_NOT_RUN = false; //在isDebugRdmaRun返回这个值的时候,不使用rdma发送,也就是假装发送
    public static boolean DEBUG_RDMA_RUN = true; //真的使用rdma发送

    public static void setRemoteRdmaAddress(String remoteRdmaAddress) {
        RdfsClient.remoteRdmaAddress = remoteRdmaAddress;
    }

    public static String getRemoteRdmaAddress() {
        if(remoteRdmaAddress == null)
            return RdfsConstants.DEFAULT_DEBUG_IP;
        return remoteRdmaAddress;
    }


    public static void setRemoteRdmaDirectory(String remoteRdmaDirectoryk) {
        RdfsClient.remoteRdmaDirectory = remoteRdmaDirectory;
    }

    public static String getRemoteRdmaDirectory() {
        return remoteRdmaDirectory;
    }


    public static void setAgentDirTreeDup(HashSet<String> agentDirTreeDup) {
        RdfsClient.agentDirTreeDup = agentDirTreeDup;
    }

    public static HashSet<String> getAgentDirTreeDup() {
        return agentDirTreeDup;
    }


    public static boolean isDebugRdmaRun() {
//        return DEBUG_RDMA_NOT_RUN;
        return DEBUG_RDMA_RUN;
    }

    public static void main(String[] args) {
        try {
            new RdfsClient().connect("127.0.0.1", 8080);
//            new RdfsClient().connect("219.228.135.215", 8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect(String host, int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture future = null;
        try {
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

            future = bootstrap.connect(host, port).sync();
            Channel channel = future.channel();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String command = null;

            /*
            client一直读取用户的输入,收到用户的输入后:
            1. 对输入进行检查,并提取出用户的操作类型,比如upload、download、delete
            2. 指令发送给server

             */
            ClientToAgentMsg msg;
            while (true) {
                Thread.sleep(500); //没有任何意义,只是为了等待收到消息之后才显示$:
                System.out.printf("$: ");
                command = in.readLine();
                int result = CommandUtil.parseStrCommand(command);
                if (result == COMMAND_UPLOAD_OK) { //到这里,文件已经被传输到远程了
//                    channel.writeAndFlush(command + "\n");
                    ArrayList<String> fileNames = RdmaUtil.getFilePaths();
                    msg = new ClientToAgentMsg();
                    /*
                    需要把这次Client传输的文件列表告诉Agent好让Agent更新目录树。
                    这里只能新建立一个ArrayList对象,否则数据还没发送到Agent,fileNames.clear()方法会把数据清空,Agent收到的数据就是空的.fileNames在每次发送完都会清空。
                     */
                    ArrayList<String> sendFileNames = new ArrayList<>();
                    for (String tmp : fileNames) {
                        String substring = tmp.substring(RdfsConstants.NVM_PATH_LENGTH);
                        sendFileNames.add(substring);
                    }
//                    Collections.copy(sendFileNames, fileNames);
                    msg.setCommandStr(command);
                    msg.setFileNames(sendFileNames);
                    msg.setRemoteRdmaAddress(RdfsClient.getRemoteRdmaAddress());
                    System.out.println("RdfsClient in connect() before send: " + msg);
                    channel.writeAndFlush(msg);
                    fileNames.clear();
                } else if (result == COMMAND_LIST_OK) {
                    msg = new ClientToAgentMsg();
                    msg.setCommandStr(command);
                    channel.writeAndFlush(msg);
                } else if (result == COMMAND_DELETT_OK) {
                    msg = new ClientToAgentMsg();
                    msg.setCommandStr(command);
                    channel.writeAndFlush(msg);
                } else if(result == COMMAND_GET_OK){
//                    System.out.println("");
                    String tmp, remotePath, localPath;
                    String[] split = command.split(" ");
                    tmp = split[1];
                    if("-f".equalsIgnoreCase(tmp)){ //有-f选项,无需判断目录是否存在,但是还是需要确定是否有权限创建目录
                        remotePath = split[2];
                        localPath = split[3];
                    }else{
                        remotePath = split[1];
                        localPath = split[2];
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("get ");
                    sb.append(remotePath + " ");
                    sb.append(localPath);
                    msg = new ClientToAgentMsg();
                    msg.setCommandStr(sb.toString());
//                    msg.setRemoteRdmaAddress("192.168.100.110");
                    channel.writeAndFlush(msg);

                } else if(result == COMMAND_HELP_OK){

                } else if(result == COMMAND_EXIT_OK){
                    System.out.println("RDFS exited!");
                    break;
                } else if (result == COMMAND_NULL) {
                    System.out.println("Command can not be empty");
                } else if (result == COMMAND_UNSUPPORTED) {
                    System.out.println("Command: " + command + " not supported");
                } else if (result == COMMAND_UNKNOWN) {
                    System.out.println("Command: " + command + " unknown");
                } else if (result == COMMAND_ILLEGAL) {
                    System.out.println("Command: " + command + " illegal");
                }

            }


        } finally {
            future.channel().closeFuture().sync();
            group.shutdownGracefully();
        }
    }


}
