package com.springboot.nettty.im.server.handler;

import com.springboot.nettty.im.server.NettyChannelManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description：实现客户端Channel断开连接、异常时的处理。
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/19
 */
@Slf4j
@Component
@ChannelHandler.Sharable //标记当前这个ChannelHandler可以被多个Channel使用
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private NettyChannelManager channelManager;

    /**
     * 在客户端和服务端建立连接完成时，调用{@link NettyChannelManager#addChannel(Channel)}，添加到管理容器中
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //从管理容器中添加
        channelManager.addChannel(ctx.channel());
    }

    /**
     * 在客户端断开连接时，调用{@link NettyChannelManager#remove(Channel)}，从管理容器中移除
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        //从管理容器中移除
        channelManager.remove(ctx.channel());
    }

    /**
     * 在处理Channel事件发生异常时，调用{@link Channel#close()}断开和客户端的连接
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("[exceptionCaught]连接[{}]发生异常" , ctx.channel().id() ,cause);
        ctx.channel().close();
    }
}
