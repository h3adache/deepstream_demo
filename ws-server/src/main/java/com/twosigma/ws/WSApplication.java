package com.twosigma.ws;

import com.twosigma.ws.service.MarketDataService;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WSApplication {
    public static void main(String[] args) {
        SpringApplication.run(WSApplication.class, args);
    }

    @Bean
    public ResourceConfig restPoints() {
        final ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(MarketDataService.class);
        return resourceConfig;
    }
}
