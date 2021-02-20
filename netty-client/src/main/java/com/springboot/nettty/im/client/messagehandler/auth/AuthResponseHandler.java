package com.springboot.nettty.im.client.messagehandler.auth;

import com.springboot.nettty.im.common.dispatcher.MessageHandler;
import com.springboot.nettty.im.server.message.auth.AuthResponse;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Description：认证响应处理
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/20
 */
@Component
@Slf4j
public class AuthResponseHandler implements MessageHandler<AuthResponse> {
    @Override
    public void execute(Channel channel, AuthResponse message) {
        log.info("[AuthResponseHandler#execute] [认证结果：{}]" , message);
    }

    @Override
    public String getType() {
        return AuthResponse.TYPE;
    }
}
