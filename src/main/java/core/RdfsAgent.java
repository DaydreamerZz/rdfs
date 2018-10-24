package core;

import utils.AgentLogThread;
import utils.AgentLogUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author : Bruce Zhao
 * @email  : zhzh402@163.com
 * @date   : 2018/3/26 12:20
 * @desc   : 使用JBoss Marshalling的netty server
 */
public class RdfsAgent {

    public static boolean syncFlag = false;

    public static boolean getSyncFlag() {
        return syncFlag;
    }

    public static void setSyncFlag(boolean syncFlag) {
        RdfsAgent.syncFlag = syncFlag;
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        /*
         * 记录下可用的存储服务器IP地址,这里是写死的.之后可以通过存储服务器与Agent的心跳消息确定可用的存储服务器IP地址.
         */
<<<<<<< HEAD
        Storage.add("192.168.0.100");
        Storage.add("192.168.0.100");
//        Storage.add("192.168.100.100");

        //根据文件,重建agent维护的目录树结构
        //public static final String RDMA_DIRTREE_LOG_FILE = "/opt/rdfs/rdma_dirtree_log_file"; 日志文件所在的位置
=======
        Storage.add("192.168.100.110");
        Storage.add("192.168.100.110");

        //根据文件,重建agent维护的目录树结构
>>>>>>> 9843dba7e0f5e316e7f9ddbd6e635b055ee93e74
        AgentLogUtil.initAgentDirTree();


        new RdfsAgent().bind(port);
    }

    public void bind(int port) throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ProtobufDecoderHandler());

            ChannelFuture future = bootstrap.bind(port).sync();

            Thread thread = new Thread(new AgentLogThread());
            thread.start();

            future.channel().closeFuture().sync();

        }finally {
            //关闭的时候同步日志到文件
            AgentLogUtil.logSync();

            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ProtobufDecoderHandler extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder()); //in
            socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder()); //out
            socketChannel.pipeline().addLast(new AgentHandler()); //in
        }
    }

}
