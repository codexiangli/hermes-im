package com.codexiangli.im.core.dispatcher.service;

import com.codexiangli.im.common.api.proto.BaseCommand;
import com.codexiangli.im.common.api.proto.CommandMessage;
import com.codexiangli.im.common.api.proto.Request;
import com.codexiangli.im.core.connection.Connection;
import com.codexiangli.im.core.connection.ConnectionPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @author lixiang
 * @since 2023/2/28
 */
@Component
@Slf4j
public class PulsarDispatcher {

    @Autowired
    private PulsarClient pulsarClient;

    @PostConstruct
    public void initConsumer() {
        ConsumerBuilder<Request> consumerBuilder = pulsarClient
                .newConsumer(Schema.PROTOBUF(Request.class))
                .topic("single-message-topic")
                .subscriptionType(SubscriptionType.Shared)
                .consumerName("single-message-topic-consumer")
                .subscriptionName("single-message-topic-subscription")
                .messageListener((consumer, msg) -> {
                    Request request = msg.getValue();
                    try {
                        BaseCommand baseCommand = request.getCmd();
                        CommandMessage commandMessage = baseCommand.getMsg();
                        // 这里先简单判断下用户在不在线 简单处理下 后续完善
                        Connection connection = ConnectionPool.getConnectionByUser(commandMessage.getToUserId());
                        if (Objects.nonNull(connection)) {
                            connection.getChannel().writeAndFlush(request);
                            consumer.acknowledge(msg);
                            log.info("consumer-{} ack message payload:{} complete", consumer, request);
                        } else {
                            log.info("用户{}不在线，暂不投递消息", commandMessage.getToUserId());
                            consumer.negativeAcknowledge(msg);
                            log.info("consumer-{} nack message payload:{} complete", consumer, request);
                        }
                        log.info("consumer-{} start receive message payload:{}", consumer, request);
                        /* method.invoke(holder.getBean(), pulsarMessage);*/
                    } catch (Exception e) {
                        log.info("consumer-{}  process message payload:{} fail", consumer, request);
                        consumer.negativeAcknowledge(msg);
                        log.info("consumer-{} nack message payload:{} complete", consumer, request);
                    }
                })
                .enableRetry(true);
        try {
            Consumer<Request> consumer = consumerBuilder.subscribe();
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
    }
}
