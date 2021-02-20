package com.springboot.nettty.im.server.messagehandler.chat;

import com.springboot.nettty.im.common.codec.Invocation;
import com.springboot.nettty.im.common.dispatcher.MessageHandler;
import com.springboot.nettty.im.server.NettyChannelManager;
import com.springboot.nettty.im.server.message.chat.ChatRedirectToUserRequest;
import com.springboot.nettty.im.server.message.chat.ChatSendResponse;
import com.springboot.nettty.im.server.message.chat.ChatSendToOneRequest;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description：处理客户端私聊请求
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/20
 */
@Slf4j
@Component
public class ChatSendToOneHandler implements MessageHandler<ChatSendToOneRequest> {

    @Autowired
    private NettyChannelManager nettyChannelManager;


    @Override
    public void execute(Channel channel, ChatSendToOneRequest message) {
        //假装直接成功
        ChatSendResponse chatSendResponse = new ChatSendResponse().setMsgId(message.getMsgId()).setCode(0);
        channel.writeAndFlush(new Invocation(ChatSendResponse.TYPE , chatSendResponse));

        //创建转发的消息，发送给指定用户
        ChatRedirectToUserRequest chatRedirectToUserRequest = new ChatRedirectToUserRequest().setContent(message.getContent()).setMsgId(message.getMsgId());
        nettyChannelManager.send(message.getToUser(),new Invocation(ChatRedirectToUserRequest.TYPE , chatRedirectToUserRequest));
    }

    @Override
    public String getType() {
        return ChatSendToOneRequest.TYPE;
    }
}
