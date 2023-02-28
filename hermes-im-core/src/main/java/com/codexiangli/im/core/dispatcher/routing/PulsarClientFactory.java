package com.codexiangli.im.core.dispatcher.routing;

import com.codexiangli.im.core.dispatcher.routing.config.PulsarConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

/**
 * @author lixiang
 * @since 2023/2/28
 */
@Slf4j
public class PulsarClientFactory {

    public static PulsarClient createPulsarClient(PulsarConfig pulsarConfig) {
        PulsarClient client = null;
        try {
            client = PulsarClient.builder()
                    .serviceUrl("pulsar://localhost:6650")
                    .build();
        } catch (PulsarClientException e) {
            log.error("pulsar client init error", e);
        }
        return client;
    }
}
