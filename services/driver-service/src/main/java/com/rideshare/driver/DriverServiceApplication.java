package com.rideshare.driver;

import com.rideshare.commons.kafka.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.rideshare")
@EnableScheduling
public class DriverServiceApplication implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DriverServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DriverServiceApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Driver Service started — consuming from topics: {}, {}",
                Topics.ORDER_REQUESTED, Topics.ORDER_CANCELLED);
    }
}
