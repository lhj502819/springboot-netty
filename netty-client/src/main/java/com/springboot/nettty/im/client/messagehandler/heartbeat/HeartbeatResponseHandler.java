package com.springboot.nettty.im.client.messagehandler.heartbeat;

import com.springboot.nettty.im.common.dispatcher.MessageHandler;
import com.springboot.nettty.im.server.message.heartbeat.HeartbeatResponse;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/20
 */
@Slf4j
@Component
public class HeartbeatResponseHandler implements MessageHandler<HeartbeatResponse> {
    @Override
    public void execute(Channel channel, HeartbeatResponse message) {
        log.info("[execute][收到连接({}) 的心跳响应]", channel.id());
    }

    @Override
    public String getType() {
        return HeartbeatResponse.TYPE;
    }
}
