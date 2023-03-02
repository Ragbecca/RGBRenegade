package com.ragbecca.rgbmailservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RgbMailServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RgbMailServiceApplication.class, args);
    }

}
