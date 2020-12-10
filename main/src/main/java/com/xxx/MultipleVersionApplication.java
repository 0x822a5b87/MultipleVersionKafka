package com.xxx;

import com.xxx.kafka.MessageProducer;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class MultipleVersionApplication {

    public static void main(String[] args)
            throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        File esV1 = new File("kafka-client-0.9/target/kafka-client-0.9-1.0-SNAPSHOT.jar");
        File esV2 = new File("kafka-client-2.0/target/kafka-client-2.0-1.0-SNAPSHOT.jar");

        System.out.println("esV1 : " + esV1.exists());
        System.out.println("esV2 : " + esV2.exists());

        ClassLoader loader1 = new URLClassLoader(new URL[] {esV1.toURI().toURL()});
        ClassLoader loader2 = new URLClassLoader(new URL[] {esV2.toURI().toURL()});

        Class<?> v09 = loader1.loadClass("com.xxx.kafka.KafkaProducerV09");
        Class<?> v20 = loader2.loadClass("com.xxx.kafka.KafkaProducerV20");

        MessageProducer<String, String> v09Instance = buildMessageProducer(v09, "127.0.0.1:9092");
        MessageProducer<String, String> v20Instance = buildMessageProducer(v20, "127.0.0.1:9092");

        System.out.println(v09Instance.version());
        System.out.println(v20Instance.version());
    }

    private static MessageProducer<String, String> buildMessageProducer(Class<?> clazz, String bootstrapServer)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Constructor<?> constructor = clazz.getDeclaredConstructor(String.class);
        return (MessageProducer<String, String>) constructor.newInstance(bootstrapServer);
    }
}
