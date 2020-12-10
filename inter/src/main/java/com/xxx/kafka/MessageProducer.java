package com.xxx.kafka;

/**
 * @author 0x822a5b87
 */
public interface MessageProducer <K, V> {
    void send(String topic, K key, V value);

    String version();
}
