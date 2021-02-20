package com.springboot.nettty.im.server.message.chat;

import com.springboot.nettty.im.common.dispatcher.Message;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Description：聊天发送消息结果响应
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/20
 */
@Data
@ToString
@Accessors(chain = true)
public class ChatSendResponse implements Message {

    public static final String TYPE = "CHAT_SEND_RESPONSE";

    /**
     * 消息ID
     */
    private String msgId;

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应提示
     */
    private String message;

}
