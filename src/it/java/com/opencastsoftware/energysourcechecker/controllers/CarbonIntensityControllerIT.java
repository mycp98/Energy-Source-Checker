package com.opencastsoftware.energysourcechecker.controllers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@SpringBootTest
public class CarbonIntensityControllerIT {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void should_return_response(){
        String test = restTemplate.getForObject("http://localhost:8081/api/test", String.class);
        assert Objects.equals(test, "Hello, Im a WireMock response");
    }

}
