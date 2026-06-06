package dev.javarush.feeder.sync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FeedSyncClient {
    static void main(String[] args) {
        SpringApplication.run(FeedSyncClient.class, args);
    }
}
