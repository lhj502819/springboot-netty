package com.springboot.nettty.im.client.handler;

import com.springboot.nettty.im.common.codec.InvocationDecoder;
import com.springboot.nettty.im.common.codec.InvocationEncoder;
import com.springboot.nettty.im.common.dispatcher.MessageDispatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Netty的 {@link ChannelHandler} 组件，用来处理Channel的各种事件，这里的事件很广泛，可以是连接、数据读写、异常、数据转换等。
 * {@link ChannelHandler}有很多子类，有个非常特殊件的{@link ChannelInitializer}，它用于Channel创建时，实现自定义的初始化逻辑
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/19
 */
@Component
public class NettyClinetHandlerInitializer extends ChannelInitializer<Channel> {

    /**
     * 心跳超时时间
     */
    private static final Integer READ_TIME_OUT_SECONDS = 60;

    @Autowired
    private NettyClientHandler nettyServerHandler;

    @Autowired
    private MessageDispatcher messageDispatcher;

    /**
     * 在每一个客户端与服务端建立完成连接时，服务端会创建一个Channel与之对应，
     * 此时会执行{@link NettyClinetHandlerInitializer#initChannel(Channel)}，进行自定义的初始化
     *
     * @param ch
     * @throws Exception
     */
    @Override
    protected void initChannel(Channel ch) throws Exception {
        //获取Channel的Channelpipline
        //调用Channel的#pipeline方法，获得客户端的Channel对应的ChannelPipeline，
        // ChannelPipeline由一系列的ChannelHandler组成，或者说是ChannelHandler链，
        // 这样Channel上所有的事件都会经过ChannelPipeline，被其上的ChannelHandler所处理
        ChannelPipeline pipeline = ch.pipeline();
        //添加一堆NettyServerHandle到ChannelPipeline
        pipeline
                //空闲监测
                .addLast(new IdleStateHandler(READ_TIME_OUT_SECONDS , 0 , 0))
                .addLast(new ReadTimeoutHandler(READ_TIME_OUT_SECONDS))
                //编码器
                .addLast(new InvocationEncoder())
                //解码器
                .addLast(new InvocationDecoder())
                //消息分发器
                .addLast(messageDispatcher)
                //服务端处理器
                .addLast(nettyServerHandler);
    }
}
