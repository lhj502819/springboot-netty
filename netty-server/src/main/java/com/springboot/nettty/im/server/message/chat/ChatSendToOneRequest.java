package com.springboot.nettty.im.server.message.chat;

import com.springboot.nettty.im.common.dispatcher.Message;
import lombok.Data;
import lombok.ToString;

/**
 * Description：指定人的私聊消息的请求
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/20
 */
@Data
@ToString
public class ChatSendToOneRequest implements Message {

    public static final String TYPE = "CHAT_SEND_TO_ONE_REQUEST";

    /**
     * 发送给的用户
     */
    private String toUser;

    /**
     * 消息编号
     */
    private String msgId;

    /**
     * 内容
     */
    private String content;

}
