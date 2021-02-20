package com.springboot.nettty.im.server.message.heartbeat;

import com.springboot.nettty.im.common.dispatcher.Message;

/**
 * Description：心跳消息
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/20
 */
public class HeartbeatRequest implements Message {

    public static final String TYPE = "HEARTBEAT_REQUEST";

    @Override
    public String toString() {
        return "HeartbeatRquest{}";
    }
}
