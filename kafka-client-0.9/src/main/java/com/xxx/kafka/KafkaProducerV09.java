package com.xxx.kafka;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.utils.AppInfoParser;

/**
 * @author 0x822a5b87
 */
public class KafkaProducerV09<K, V> implements MessageProducer<K, V> {

    private final KafkaProducer<K, V> producer;

    public KafkaProducerV09(String bootstrapServer) {
        Properties props = new Properties();
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("client.id", "hidden-producer-client-id-1");
        props.put("bootstrap.servers", bootstrapServer);
        //生产者发送消息
        this.producer = new KafkaProducer<K, V>(props);
    }

    @Override
    public void send(String topic, K key, V value) {
        ProducerRecord<K, V> record = new ProducerRecord<>(topic, key, value);
        producer.send(record);
    }

    @Override
    public String version() {
        return AppInfoParser.getVersion();
    }
}
