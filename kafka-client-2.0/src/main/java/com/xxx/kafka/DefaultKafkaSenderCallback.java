package com.xxx.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hangyudu
 * @date 2019-08-27
 */
public class DefaultKafkaSenderCallback<V> implements Callback {

    private final static Logger LOG = LoggerFactory.getLogger(DefaultKafkaSenderCallback.class);

    private final V originalMessage;

    public DefaultKafkaSenderCallback(V originalMessage) {
        this.originalMessage = originalMessage;
    }

    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (exception != null) {
            LOG.error("exception = [{}]", exception.getMessage());
            LOG.error("", exception);
        }
        trace(metadata);
    }

    private void trace(RecordMetadata metadata) {
        if (LOG.isTraceEnabled()) {
            if (metadata == null || originalMessage == null) {
                LOG.info("{}, {}", metadata == null, originalMessage == null);
            } else {
                LOG.trace("send message : offset = [{}], partition = [{}], topic = [{}]\nmessage = [{}]",
                        metadata.offset(), metadata.partition(), metadata.topic(),
                        originalMessage.toString());
            }
        }
    }
}
