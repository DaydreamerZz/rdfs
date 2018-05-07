package com.ecnu.zz.core;

import com.ecnu.zz.utils.CommandUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 18-5-6 下午7:47
 * @desc :
 */
public class RdfsServer {

    public static void main(String[] args){
        try {
            new RdfsServer().connect("127.0.0.1", 8080);
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

//                            socketChannel.pipeline().addLast(new ClientHandler()); //in
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


            }



        }finally {
//            future.channel().closeFuture().sync();
            group.shutdownGracefully();
        }
    }

}
