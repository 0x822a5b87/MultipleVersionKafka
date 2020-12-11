package com.xxx;

import com.xxx.kafka.MessageProducer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class KafkaWorkerThread implements Runnable {

    private final ClassLoader classLoader;
    private final Class<?>    clazz;

    public KafkaWorkerThread(ClassLoader classLoader, Class<?> clazz) {
        this.classLoader = classLoader;
        this.clazz = clazz;
    }

    @Override
    public void run() {
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            MessageProducer<String, String> producer = buildMessageProducer();
            producer.init();
            producer.send("hangyudu_test", "key", "message from " + producer.version());
            Thread.sleep(10);
            System.out.println(producer.version());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private MessageProducer<String, String> buildMessageProducer()
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Constructor<?> constructor = clazz.getDeclaredConstructor(String.class);
        return (MessageProducer<String, String>) constructor.newInstance("127.0.0.1:9092");
    }
}
