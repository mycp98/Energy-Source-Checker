package com.opencastsoftware.energysourcechecker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class CarbonIntensityClientConfig {

    @Value("${carbon-intensity.base-url}")
    private String BASE_URL;

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplateBuilder()
                .rootUri(BASE_URL)
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
    }
}
