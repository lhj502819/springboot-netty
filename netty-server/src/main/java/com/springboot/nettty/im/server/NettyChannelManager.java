package com.springboot.nettty.im.server;

import com.springboot.nettty.im.common.codec.Invocation;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 1、客户端Channel的管理
 * 2、向客户端Channel发送消息
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/19
 */
@Slf4j
@Component
public class NettyChannelManager {

    /**
     * {@link io.netty.channel.Channel#attr(AttributeKey)}属性中
     */
    private static final AttributeKey<String> CHANNEL_ATTR_KEY = AttributeKey.newInstance("user");

    /**
     * channel 映射
     */
    private ConcurrentMap<ChannelId , Channel> channels = new ConcurrentHashMap<>();

    /**
     * 用户与Channel的映射
     *
     * 通过它可以获取对应用户的Channel，这样可以向指定用户发送消息
     */
    private ConcurrentMap<String , Channel> userChannels = new ConcurrentHashMap<>();

    public void addChannel(Channel channel){
        channels.put(channel.id() , channel);
        log.info("[一个连接[{}]加入]" , channel.id());
    }

    /**
     * 添加一个用户到 {@link #userChannels}中
     * @param channel Channel
     * @param user 用户
     */
    public void addUser(Channel channel , String user){
        if(!channels.containsKey(channel.id())){
            log.error("[addUser][连接{}]不存在" , channel.id());
            return;
        }
        //设置属性
        channel.attr(CHANNEL_ATTR_KEY).set(user);
        //添加到userChannels
        userChannels.put(user , channel);
    }

    /**
     * 将Channel从 {@link #channels} 和 {@link #userChannels}中移除
     */
    public void remove(Channel channel){
        channels.remove(channel);
        //移除userChannels
        if(channel.hasAttr(CHANNEL_ATTR_KEY)){
            userChannels.remove(channel.attr(CHANNEL_ATTR_KEY).get());
        }
        log.info("[remove]一个连接[{}]离开" , channel.id());
    }

    /**
     * 向指定用户发消息
     * @param user 用户
     * @param invocation 消息
     */
    public void send(String user , Invocation invocation){
        //获取对应用户的Channel
        Channel userChannel = userChannels.get(user);
        if(userChannel == null){
            log.error("[send]用户[{}]连接不存在" , user);
            return;
        }
        if(!userChannel.isActive()){
            log.error("[send]连接[{}]未激活" , userChannel.id());
        }
        //发送消息
        userChannel.writeAndFlush(invocation);
    }

    /**
     * 向所有用户发送消息
      * @param invocation 消息
     */
    public void sendAll(Invocation invocation){
        for (Channel channel : channels.values()) {
            if(!channel.isActive()){
                log.error("[send]连接[{}]未激活" , channel.id());
            }
            //发送消息
            channel.writeAndFlush(invocation);
        }
    }



}
