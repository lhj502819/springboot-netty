package com.springboot.nettty.im.common.codec;

import com.alibaba.fastjson.JSON;
import com.springboot.nettty.im.common.dispatcher.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Description：通信协议消息实体
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @Date 2021/2/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Invocation {
    /**
     * 类型 ：用于匹配对应的消息处理器。如果类比HTTP协议，type属性相当于请求地址
     */
    private String type;

    /**
     * 消息：JSON格式
     */
    private String message;

    public Invocation(String type , Message message){
        this.type = type;
        this.message = JSON.toJSONString(message);
    }


}
