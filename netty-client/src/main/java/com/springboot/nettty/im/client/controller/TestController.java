package com.springboot.nettty.im.client.controller;

import com.springboot.nettty.im.client.NettyClient;
import com.springboot.nettty.im.common.codec.Invocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/20
 */
@RequestMapping("/test")
@RestController
public class TestController {

    @Autowired
    private NettyClient nettyClient;

    @PostMapping("/mock")
    public String mock(String type , String message){
        //创建Invocation对象
        Invocation invocation = new Invocation(type , message);
        //发送消息
        nettyClient.send(invocation);
        return "success";
    }

}
