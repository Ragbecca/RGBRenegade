package com.ragbecca.rgbdiscoveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class RgbDiscoveryServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RgbDiscoveryServerApplication.class, args);
    }

}
