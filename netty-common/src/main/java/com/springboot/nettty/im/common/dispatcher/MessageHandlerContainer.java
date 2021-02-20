package com.springboot.nettty.im.common.dispatcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 作为MessageHandler的容器
 *
 * @author lihongjian
 * @since 2021/2/19
 */
@Slf4j
public class MessageHandlerContainer implements InitializingBean {

    /**
     * 消息类型与 MessageHandler
     */
    private final Map<String, MessageHandler> handlers = new HashMap<>();

    /**
     * 通过@Autowired可以依赖注入BeanFactory、ResourceLoader、ApplicationContext、ApplicationEventPublisher Bean实例
     */
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        //通过ApplicationContext 获得所有 MessageHandler Bean
        applicationContext.getBeansOfType(MessageHandler.class)
                .values().forEach(messageHandler -> handlers.put(messageHandler.getType(), messageHandler));
        log.info("[afterPropertiesSet][消息处理器数量：]", handlers.size());
    }

    /**
     * 获取类型对应的 MessageHandler
     *
     * @param type 类型
     * @return {@link MessageHandler}
     */
    MessageHandler getMessageHandler(String type) {
        MessageHandler messageHandler = handlers.get(type);
        if (messageHandler == null) {
            throw new IllegalArgumentException(String.format("类型(%s)找不到匹配的 MessageHandler 处理器", type));
        }
        return messageHandler;
    }

    /**
     * 获得{@link MessageHandler} 处理的消息类
     *
     * @param handler 处理器
     * @return 消息类
     */
    static Class<? extends Message> getMessageClass(MessageHandler handler) {
        //获得Bean对应的Class类名，因为有可能被AOP代理过。
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(handler);
        //获得接口的 TYPE数组
        Type[] interfaces = targetClass.getGenericInterfaces();
        Class<?> superclass = targetClass.getSuperclass();
        //此处是以父类的接口为准
        while ((Objects.isNull(interfaces) || 0 == interfaces.length) && Objects.nonNull(superclass)) {
            interfaces = superclass.getGenericInterfaces();
            superclass = superclass.getSuperclass();
        }
        if (Objects.nonNull(interfaces)) {
            //遍历 interfaces 数组
            for (Type type : interfaces) {
                //要求Type是泛型参数
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    //要求是MessageHandler接口
                    if (Objects.equals(parameterizedType.getRawType(), MessageHandler.class)) {
                        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                        //取首个元素
                        if (Objects.nonNull(actualTypeArguments) && actualTypeArguments.length > 0) {
                            return (Class<Message>) actualTypeArguments[0];
                        } else {
                            throw new IllegalArgumentException(String.format("类型(%s) 获得不到消息类型", handler));
                        }
                    }
                }
            }
        }
        throw new IllegalArgumentException(String.format("类型(%s) 获得不到消息类型", handler));
    }
}
