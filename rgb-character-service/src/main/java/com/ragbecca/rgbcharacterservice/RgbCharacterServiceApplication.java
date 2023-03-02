package com.ragbecca.rgbcharacterservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RgbCharacterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RgbCharacterServiceApplication.class, args);
    }

}
