package com.springboot.nettty.im.server.messagehandler.heartbeat;

import com.springboot.nettty.im.common.codec.Invocation;
import com.springboot.nettty.im.common.dispatcher.MessageHandler;
import com.springboot.nettty.im.server.message.heartbeat.HeartbeatRequest;
import com.springboot.nettty.im.server.message.heartbeat.HeartbeatResponse;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Description：
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/20
 */
@Slf4j
@Component
public class HeartbeatRequestHandler implements MessageHandler<HeartbeatRequest> {
    @Override
    public void execute(Channel channel, HeartbeatRequest message) {
        log.info("[HeartbeatRequestHandler#execute] [收到连接({})的心跳请求]" , channel.id());
        //响应心跳
        HeartbeatResponse response = new HeartbeatResponse();
        channel.writeAndFlush(new Invocation(HeartbeatResponse.TYPE , response));
    }

    @Override
    public String getType() {
        return HeartbeatRequest.TYPE;
    }
}
