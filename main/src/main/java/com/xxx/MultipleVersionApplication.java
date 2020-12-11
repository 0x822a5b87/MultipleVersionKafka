package com.xxx;

import com.xxx.kafka.MessageProducer;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class MultipleVersionApplication {

    public static void main(String[] args)
            throws MalformedURLException, ClassNotFoundException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        File esV1 = new File("kafka-client-0.9/target/kafka-client-0.9-1.0-SNAPSHOT.jar");
        File esV2 = new File("kafka-client-2.0/target/kafka-client-2.0-1.0-SNAPSHOT.jar");

        assert esV1.exists();
        assert esV2.exists();

        System.out.println("esV1 url = " + esV1.toURI().toURL());
        System.out.println("esV2 url = " + esV2.toURI().toURL());


        ClassLoader loader1 = new URLClassLoader(new URL[] {esV1.toURI().toURL()});
        ClassLoader loader2 = new URLClassLoader(new URL[] {esV2.toURI().toURL()});

        Class<?> v09 = loader1.loadClass("com.xxx.kafka.KafkaProducerV09");
        Class<?> v20 = loader2.loadClass("com.xxx.kafka.KafkaProducerV20");

        MessageProducer<String, String> v09Instance = (MessageProducer<String, String>)
                v09.getDeclaredConstructor(String.class).newInstance("127.0.0.1:9092");

        MessageProducer<String, String> v20Instance = (MessageProducer<String, String>)
                v20.getDeclaredConstructor(String.class).newInstance("127.0.0.1:9092");

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(loader1);
        v09Instance.init();
        Thread.currentThread().setContextClassLoader(loader2);
        v20Instance.init();
        Thread.currentThread().setContextClassLoader(contextClassLoader);

        v09Instance.send("hangyudu_test", "key", "message from " + v09Instance.version());
        v20Instance.send("hangyudu_test", "key", "message from " + v20Instance.version());

        Thread.sleep(1000);
    }

}
