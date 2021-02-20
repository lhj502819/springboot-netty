package com.springboot.nettty.im.server.message.auth;

import com.springboot.nettty.im.common.dispatcher.Message;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Description：消息 - 用户认证响应
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/20
 */
@Data
@Accessors(chain = true)
@ToString
public class AuthResponse implements Message {

    public static final  String TYPE = "AUTH_RESPONSE";

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应提示
     */
    private String message;

}
