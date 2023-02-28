package com.codexiangli.im.server.pulsar;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

/**
 * @author lixiang
 * @since 2022/9/14
 */
public class PulsarConfig {


    public static PulsarClient getPulsarClient(){
        return PulsarClientLoader.PULSAR_CLIENT;
    }

    public static class PulsarClientLoader {
        // 无意义的单例
        public static PulsarClient PULSAR_CLIENT;

        static {
            try {
                PULSAR_CLIENT = PulsarClient.builder()
                        .serviceUrl("pulsar://localhost:6650")
                        .build();
            } catch (PulsarClientException e) {
                e.printStackTrace();
            }
        }
    }

}
