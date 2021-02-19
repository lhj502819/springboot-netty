package com.springboot.nettty.im.common.dispatcher;

import com.alibaba.fastjson.JSON;
import com.springboot.nettty.im.common.codec.Invocation;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 消息分发器
 *
 * {@link SimpleChannelInboundHandler}是Netty定义的消息处理 ChannelHandler 抽象类，处理消息的类型T 泛型时。
 *
 * @author lihongjian
 * @since 2021/2/19
 */
@ChannelHandler.Sharable
public class MessageDispatcher extends SimpleChannelInboundHandler<Invocation> {

    @Autowired
    private MessageHandlerContainer messageHandlerContainer;

    private final ExecutorService executor = Executors.newFixedThreadPool(200);

    /**
     * 处理消息，进行转发
     * @param ctx
     * @param invocation
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Invocation invocation) throws Exception {
        //获得TYPE对应的 MessageHandler 处理器
        MessageHandler messageHandler = messageHandlerContainer.getMessageHandler(invocation.getType());
        //获得 MessageHandler 处理器的消息类
        Class<? extends Message> messageClass = MessageHandlerContainer.getMessageClass(messageHandler);
        //解析消息
        Message message = JSON.parseObject(invocation.getMessage(), messageClass);
        //执行逻辑
        executor.submit(()->{
            messageHandler.execute(ctx.channel() , message);
        });
    }





}
