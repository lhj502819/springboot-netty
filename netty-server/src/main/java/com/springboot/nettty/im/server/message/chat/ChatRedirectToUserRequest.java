package com.springboot.nettty.im.server.message.chat;

import com.springboot.nettty.im.common.dispatcher.Message;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Description：转发消息给一个用的请求
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/20
 */
@Data
@ToString
@Accessors(chain = true)
public class ChatRedirectToUserRequest implements Message {

    public static final String TYPE = "CHAT_REDIRECT_TO_USER_REQUEST";

    private String fromUser;

    private String msgId;

    private String content;

}
