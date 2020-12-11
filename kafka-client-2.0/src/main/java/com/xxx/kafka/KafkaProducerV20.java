package com.xxx.kafka;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.AppInfoParser;

public class KafkaProducerV20<K, V> implements MessageProducer<K, V> {

    private final Properties     props;
    private       Producer<K, V> producer;

    public KafkaProducerV20(String bootstrapServer) {
        props = new Properties();
        props.put("key.serializer", StringSerializer.class);
        props.put("value.serializer", StringSerializer.class);
        props.put("bootstrap.servers", bootstrapServer);
        props.put("client.id", "KafkaProducerV20");
    }

    @Override
    public void init() {
        this.producer = new KafkaProducer<K, V>(props);
    }

    @Override
    public void send(String topic, K key, V value) {
        ProducerRecord<K, V> record = new ProducerRecord<>(topic, key, value);
        producer.send(record, new DefaultKafkaSenderCallback<V>(value));
    }

    @Override
    public String version() {
        return AppInfoParser.getVersion();
    }

}
