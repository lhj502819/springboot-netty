package com.springboot.nettty.im.common.codec;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 将Invocation序列化并写入到TCP Socket中
 *
 * {@link MessageToByteEncoder}是Netty定义的编码 ChannelHandler抽象类，将泛型T消息转换成字节数组 ，最终会将ByteBuf out写入到TCP Socket中
 *
 * @author lihongjian
 * @since 2021/2/19
 */
@Slf4j
public class InvocationEncoder extends MessageToByteEncoder<Invocation> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Invocation msg, ByteBuf out) throws Exception {
        //1、将Invocation转换成byte[]数组
        byte[] content = JSON.toJSONBytes(msg);
        //写入length，后续InvocationDecoder可以根据该长度，解析到该消息，解决拆包粘包的问题
        out.writeInt(content.length);
        //写入内容
        out.writeBytes(content);
        log.info("[InvocationEncoder#encode] [连接({})]编码了一条消息" , ctx.channel().id());
    }
}
