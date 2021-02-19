package com.springboot.nettty.im.client;

import com.springboot.nettty.im.client.handler.NettyClinetHandlerInitializer;
import com.springboot.nettty.im.common.Invocation;
import com.springboot.nettty.im.server.handler.NettyServerHandlerInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
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
public class NettyClient {

    /**
     * 重连频率 ， 单位：秒
     */
    private static final Integer RECONNECT_SECONDS = 20;

    @Value("${netty.server.port}")
    private Integer serverPort;

    @Value("${netty.server.host}")
    private String serverHost;

    @Autowired
    private NettyClinetHandlerInitializer nettyServerHandlerInitializer;

    /**
     * Netty采用的是多Reactor多线程的模型，服务端可以接受更多客户端的数据读写能力。原因是
     * 1、创建专门用于接受客户端连接的bossGroup线程组，避免因为已连接的客户端的数据读写频繁，影响新的客户端的连接
     * 2、创建专门用于接收客户端读写的workerGroup线程组，多个线程进行客户端的数据读写，可以支持更多客户端
     */

    /**
     *线程组，用于客户端对服务端的连接、数据读写
     */
    private EventLoopGroup eventGroup = new NioEventLoopGroup();

    /**
     * Netty Client Channel
     */
    private volatile Channel channel;

    @PostConstruct
    public void start() throws InterruptedException {
        //创建ServerBootstrap 对象，用于NettyServer启动
        Bootstrap bootstrap = new Bootstrap();
        //设置ServerBootstrap的各种属性
        bootstrap.group(eventGroup) //设置一个EventLoopGroup 对象
        .channel(NioSocketChannel.class) // 指定Channel为客户端NioSocketChannel
        .remoteAddress(serverHost , serverPort) // 设置NettyServer的端口号
        .option(ChannelOption.SO_KEEPALIVE , true) //TCP Keepalive机制，实现TCP顶层的心跳保活机制
        .option(ChannelOption.TCP_NODELAY , true) //允许较小的数据包的发送，降低延迟
        .handler(nettyServerHandlerInitializer);
        //连接服务器，并异步等待成功，即启动客户端
        bootstrap.connect().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                //连接失败
                if(!future.isSuccess()){
                    log.error("[start] [Netty Clinet 连接服务器({}:{})失败]" , serverHost , serverPort);
                    reconnect();
                    return;
                }
                //连接成功
                channel = future.channel();
                log.info("[start] [Netty Clinet 连接服务器({}:{})成功]" , serverHost , serverPort);
            }
        });
    }

    public void reconnect(){

    }

    @PreDestroy
    public void shutdown(){
        //关闭Netty Server，客户端就不能再连接了
        if(channel != null){
            channel.close();
        }
        //优雅关闭EventLoopGroup
        eventGroup.shutdownGracefully();
    }

    /**
     * 发送消息
     * @param invocation 消息
     */
    public void send(Invocation invocation){
        if(channel == null){
            log.error("[send] 连接不存在");
            return;
        }
        if(!channel.isActive()){
            log.error("[send] [连接({})未激活]" , channel.id());
            return;
        }
        //发送消息
        channel.writeAndFlush(invocation);
    }


}
