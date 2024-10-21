package com.opencastsoftware.energysourcechecker.controllers;
import com.opencastsoftware.energysourcechecker.exceptions.CarbonIntensityClientException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CarbonIntensityControllerIT {

    @Autowired
    CarbonIntensityController carbonIntensityController;

    @Test
    public void should_return_renewable_response(){
        ResponseEntity<String> result = carbonIntensityController.getRegionalIntensity("2024-10-01", "2024-10-02", "SW12");
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo("renewable");
    }

    @Test
    public void should_return_nuclear_response(){
        ResponseEntity<String> result = carbonIntensityController.getRegionalIntensity("2024-10-01", "2024-10-02", "LA3");
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo("nuclear");
    }

    @Test
    public void should_return_non_renewable_response(){
        ResponseEntity<String> result = carbonIntensityController.getRegionalIntensity("2024-10-05", "2024-10-06", "LA3");
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo("non-renewable");
    }

    @Test
    public void should_return_out_of_range_error(){
        Exception exception = assertThrows(CarbonIntensityClientException.class, () -> {
            carbonIntensityController.getRegionalIntensity("2024-09-01", "2024-09-16", "LA3");
        });

        assertThat(exception.getMessage()).contains("The date range you have specified is greater than 14 days. Please select a smaller date range");
    }

    @Test
    public void should_return_start_date_greater_end_date_error(){
        Exception exception = assertThrows(CarbonIntensityClientException.class, () -> {
            carbonIntensityController.getRegionalIntensity("2024-08-15", "2024-08-14", "LA3");
        });

        assertThat(exception.getMessage()).contains("The start datetime should be less than the end datetime");
    }
}
