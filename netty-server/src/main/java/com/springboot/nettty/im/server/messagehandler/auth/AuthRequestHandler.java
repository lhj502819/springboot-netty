package com.springboot.nettty.im.server.messagehandler.auth;

import com.springboot.nettty.im.common.codec.Invocation;
import com.springboot.nettty.im.common.dispatcher.MessageHandler;
import com.springboot.nettty.im.server.NettyChannelManager;
import com.springboot.nettty.im.server.message.auth.AuthRequest;
import com.springboot.nettty.im.server.message.auth.AuthResponse;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Description：处理客户端认证请求
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/20
 */
@Slf4j
@Component
public class AuthRequestHandler implements MessageHandler<AuthRequest> {

    @Autowired
    private NettyChannelManager nettyChannelManager;

    @Override
    public void execute(Channel channel, AuthRequest message) {
        log.info("[AuthRequestHandler#execute] [接收到一条连接({})认证]" , channel.id());
        //如果未传递accessToken
        if(StringUtils.isEmpty(message.getAccessToken())){
            AuthResponse response = new AuthResponse().setCode(1).setMessage("认证 accessToken 未传递");
            channel.writeAndFlush(new Invocation(AuthResponse.TYPE , response));
            return;
        }

        //将用户和Channel绑定
        //直接使用accessToken 作为user
        nettyChannelManager.addUser(channel , message.getAccessToken());

        //响应认证成功
        AuthResponse response = new AuthResponse().setCode(0);
        channel.writeAndFlush(new Invocation(AuthResponse.TYPE , response));
    }

    @Override
    public String getType() {
        return AuthRequest.TYPE;
    }
}
