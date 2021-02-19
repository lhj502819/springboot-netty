package com.springboot.nettty.im.server.config;

import com.springboot.nettty.im.common.dispatcher.MessageDispatcher;
import com.springboot.nettty.im.common.dispatcher.MessageHandlerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lihongjian
 * @since 2021/2/19
 */
@Configuration
public class NettyServerConfig {

    @Bean
    public MessageDispatcher messageDispatcher(){
        return new MessageDispatcher();
    }

    @Bean
    public MessageHandlerContainer messageHandlerContainer(){
        return new MessageHandlerContainer();
    }

}
