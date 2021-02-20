package com.springboot.nettty.im.server;

import com.springboot.nettty.im.server.handler.NettyServerHandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/19
 */
@Slf4j
@Component
public class NettyServer {

    /**
     * 监听的端口号
     */
    @Value("${netty.port}")
    private Integer port;

    @Autowired
    private NettyServerHandlerInitializer nettyServerHandlerInitializer;

    /**
     * Netty采用的是多Reactor多线程的模型，服务端可以接受更多客户端的数据读写能力。原因是
     * 1、创建专门用于接受客户端连接的bossGroup线程组，避免因为已连接的客户端的数据读写频繁，影响新的客户端的连接
     * 2、创建专门用于接收客户端读写的workerGroup线程组，多个线程进行客户端的数据读写，可以支持更多客户端
     */

    /**
     * boss 线程组，用于服务端接受客户端的连接
     */
    private EventLoopGroup bossGroup = new NioEventLoopGroup();

    /**
     * worker 线程组，用于服务端接受客户端的连接
     */
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    /**
     * Netty Server Channel
     */
    private Channel channel;

    @PostConstruct
    public void start() throws InterruptedException {
        //创建ServerBootstrap 对象，用于NettyServer启动
        ServerBootstrap bootstrap = new ServerBootstrap();
        //设置ServerBootstrap的各种属性
        bootstrap.group(bossGroup, workerGroup) //设置两个EventLoopGroup对象
        .channel(NioServerSocketChannel.class) // 指定Channel为服务端NioServerSocketChannel，它是 Netty 定义的 NIO 服务端 TCP Socket 实现类。
        .localAddress(new InetSocketAddress(port)) // 设置NettyServer的端口号
        .option(ChannelOption.SO_BACKLOG , 1024) // 服务端accept队列大小。应为TCP建立连接是三次握手，所以第一次握手完成后，会添加到服务端的连接队列中
        .childOption(ChannelOption.SO_KEEPALIVE , true) //TCP Keepalive机制，实现TCP顶层的心跳保活机制
        .childOption(ChannelOption.TCP_NODELAY , true) //允许较小的数据包的发送，降低延迟
        .childHandler(nettyServerHandlerInitializer);
        //白丁端口，并同步等待成功，即启动服务端
        ChannelFuture future = bootstrap.bind().sync();
        if(future.isSuccess()){
            channel = future.channel();
            log.info("[#start][ Netty Server 启动成功，端口号[{}]]" , port);
        }
    }

    @PreDestroy
    public void shutdown(){
        //关闭Netty Server，客户端就不能再连接了
        if(channel != null){
            channel.close();
        }
        //优雅关闭两个EventLoopGroup
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }




}
