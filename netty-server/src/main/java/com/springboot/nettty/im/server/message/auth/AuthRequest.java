package com.springboot.nettty.im.server.message.auth;

import com.springboot.nettty.im.common.dispatcher.Message;
import lombok.Data;
import lombok.ToString;

/**
 * Description：消息 - 用于认证请求
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/20
 */
@Data
@ToString
public class AuthRequest implements Message {

    public static final String TYPE = "AUTH_REQUEST";

    /**
     * 认证 Token
     */
    String accessToken;



}
