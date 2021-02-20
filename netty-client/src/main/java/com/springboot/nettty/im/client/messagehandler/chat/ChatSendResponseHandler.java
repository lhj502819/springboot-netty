package com.springboot.nettty.im.client.messagehandler.chat;

import com.springboot.nettty.im.common.dispatcher.MessageHandler;
import com.springboot.nettty.im.server.message.chat.ChatSendResponse;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Description：处理服务端的聊天响应
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/20
 */
@Slf4j
@Component
public class ChatSendResponseHandler implements MessageHandler<ChatSendResponse> {
    @Override
    public void execute(Channel channel, ChatSendResponse message) {
      log.info("[ChatSendResponseHandler#execute] 发送结果：{}" , message);
    }

    @Override
    public String getType() {
        return ChatSendResponse.TYPE;
    }
}
