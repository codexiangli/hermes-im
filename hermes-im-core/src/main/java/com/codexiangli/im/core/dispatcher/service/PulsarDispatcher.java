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
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author lixiang
 * @since 2023/2/28
 */
@Component
@Slf4j
public class PulsarDispatcher implements BeanPostProcessor {

    @Autowired
    private PulsarClient pulsarClient;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
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
                        Connection connection = ConnectionPool.getConnectionByUser(commandMessage.getToUserId());
                        connection.getChannel().writeAndFlush(request);
                        log.info("consumer-{} start receive message payload:{}", consumer, request);
                       /* method.invoke(holder.getBean(), pulsarMessage);*/

                        consumer.acknowledge(msg);
                        log.info("consumer-{} ack message payload:{} complete", consumer, request);
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
        return bean;
    }
}
