package com.codexiangli.im.core.dispatcher.config;

import com.codexiangli.im.core.dispatcher.routing.PulsarClientFactory;
import org.apache.pulsar.client.api.PulsarClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lixiang
 * @since 2023/2/28
 */
@Configuration
public class PulsarConfig {

    @Bean
    public PulsarClient pulsarClient() {
        return PulsarClientFactory.createPulsarClient(null);
    }
}
