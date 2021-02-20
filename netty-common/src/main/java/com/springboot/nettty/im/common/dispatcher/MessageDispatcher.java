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
            /**
             * 为什么要丢到线程池执行逻辑？
             *
             * 答：我们在启动Netty服务端或者客户端的时候都会设置其EventGroup。EventGroup可以先简单理解成一个线程池，并且线程池的大小仅仅是 CPU数量*2
             * 每个Channel仅仅会被分配到一个线程上，进行数据的读写。并且多个Channel会共享一个线程，即使用同一个线程进行数据的读写。
             * MessageHandler的具体逻辑视线中，往往会涉及到IO处理，例如说进行数据库的读写。这样就会导致一个Channel在执行MessageHandler的过程中，
             * 阻塞了共享当前线程的其他Channel的数据读取。
             *
             * 因此，在这里创建了executor线程池，进行MessageHandler的逻辑执行，避免阻塞Channel的数据读取
             */
            messageHandler.execute(ctx.channel() , message);
        });
    }





}
