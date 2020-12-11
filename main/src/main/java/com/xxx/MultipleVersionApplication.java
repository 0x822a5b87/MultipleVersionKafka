package com.xxx;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultipleVersionApplication {

    public static void main(String[] args)
            throws MalformedURLException, ClassNotFoundException, InterruptedException {

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
        KafkaWorkerThread v09Thread = new KafkaWorkerThread(loader1, v09);
        KafkaWorkerThread v20Thread = new KafkaWorkerThread(loader2, v20);
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(v09Thread);
        service.submit(v20Thread);

        Thread.sleep(1000);
        if (!service.isShutdown()) {
            service.shutdown();
        }
    }

}
