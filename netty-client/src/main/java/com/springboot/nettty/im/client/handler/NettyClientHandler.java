package com.springboot.nettty.im.client.handler;

import com.springboot.nettty.im.client.NettyClient;
import com.springboot.nettty.im.common.codec.Invocation;
import com.springboot.nettty.im.server.message.heartbeat.HeartbeatRequest;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
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
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private NettyClient nettyClient;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //发起重连
        nettyClient.reconnect();
        //继续触发事件
        super.channelInactive(ctx);
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

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
        //空闲时，向服务端发起一次心跳
        if(event instanceof IdleStateEvent){
            log.info("[userEventTriggered][发起一次心跳]");
            HeartbeatRequest heartbeatRequest = new HeartbeatRequest();
            ctx.writeAndFlush(new Invocation(HeartbeatRequest.TYPE , heartbeatRequest))
                    .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        }else {
            super.userEventTriggered(ctx , event);
        }
    }
}
