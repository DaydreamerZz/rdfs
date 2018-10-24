package core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import utils.ServerFileCheck;
import utils.ServerFileThread;

/**
 * @author : Bruce Zhao
 * @email  : zhzh402@163.com
 * @date   : 2018/3/25 19:17
 * @desc   :
 */
public class RdfsServer {


    /*
     * Debug选项
     */
    public static boolean DEBUG_RDMA_NOT_RUN = false; //在isDebugRdmaRun返回这个值的时候,不使用rdma发送,也就是假装发送
    public static boolean DEBUG_RDMA_RUN = true; //真的使用rdma发送

    public static boolean isDebugRdmaRun() {
        return DEBUG_RDMA_NOT_RUN;
//        return DEBUG_RDMA_RUN;
    }

    public static void main(String[] args) {
        try {
            new RdfsServer().connect("127.0.0.1", 8080);
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

                            socketChannel.pipeline().addLast(new ServerHandler()); //in
                        }
                    });

            future = bootstrap.connect(host, port).sync();
            Channel channel = future.channel();

            Thread thread = new Thread(new ServerFileThread());
            thread.start();


        } finally {
            future.channel().closeFuture().sync();
            group.shutdownGracefully();
        }
    }


}
