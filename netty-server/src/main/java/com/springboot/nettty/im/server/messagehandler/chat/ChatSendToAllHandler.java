package com.springboot.nettty.im.server.messagehandler.chat;

import com.springboot.nettty.im.common.codec.Invocation;
import com.springboot.nettty.im.common.dispatcher.MessageHandler;
import com.springboot.nettty.im.server.NettyChannelManager;
import com.springboot.nettty.im.server.message.chat.ChatRedirectToUserRequest;
import com.springboot.nettty.im.server.message.chat.ChatSendResponse;
import com.springboot.nettty.im.server.message.chat.ChatSendToAllRequest;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description：处理客户端的群聊请求
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/20
 */
@Slf4j
@Component
public class ChatSendToAllHandler implements MessageHandler<ChatSendToAllRequest> {

    @Autowired
    private NettyChannelManager nettyChannelManager;

    @Override
    public void execute(Channel channel, ChatSendToAllRequest message) {
        //假装直接成功
        ChatSendResponse chatSendResponse = new ChatSendResponse().setCode(0).setMsgId(message.getMsgId());
        channel.writeAndFlush(new Invocation(ChatSendToAllRequest.TYPE,chatSendResponse));

        //创建转发消息，并广播发送
        ChatRedirectToUserRequest chatRedirectToUserRequest = new ChatRedirectToUserRequest().setMsgId(message.getMsgId()).setContent(message.getContent());
        nettyChannelManager.sendAll(new Invocation(ChatRedirectToUserRequest.TYPE , chatRedirectToUserRequest));
    }

    @Override
    public String getType() {
        return ChatSendToAllRequest.TYPE;
    }
}
