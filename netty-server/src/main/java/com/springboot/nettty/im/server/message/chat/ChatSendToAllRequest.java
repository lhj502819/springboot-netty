package com.springboot.nettty.im.server.message.chat;

import com.springboot.nettty.im.common.dispatcher.Message;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Description：发送给所有人的群聊消息请求
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/20
 */
@Data
@ToString
@Accessors(chain = true)
public class ChatSendToAllRequest implements Message {

    public static final String TYPE = "CHAT_SEND_TO_ALL_REQUEST";

    private String msgId;

    private String content;


}
