package com.springboot.nettty.im.server.message.heartbeat;

import com.springboot.nettty.im.common.dispatcher.Message;

/**
 * Description：消息-心跳响应
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/20
 */
public class HeartbeatResponse implements Message {

    /**
     * 类型
     */
    public static final String TYPE = "HEARTBEAT_RESPONSE";

    @Override
    public String toString() {
        return "HeartbeatResponse{}";
    }
}
