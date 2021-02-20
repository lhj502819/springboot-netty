package com.springboot.nettty.im.client.messagehandler.chat;

import com.springboot.nettty.im.common.dispatcher.MessageHandler;
import com.springboot.nettty.im.server.message.chat.ChatRedirectToUserRequest;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Description：处理服务端的转发消息的请求
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/20
 */
@Slf4j
@Component
public class ChatRedirectToUserRequestHandler implements MessageHandler<ChatRedirectToUserRequest> {
    @Override
    public void execute(Channel channel, ChatRedirectToUserRequest message) {
      log.info("[ChatRedirectToUserRequestHandler#execute] 收到消息 ：{}" , message);
    }

    @Override
    public String getType() {
        return ChatRedirectToUserRequest.TYPE;
    }
}
