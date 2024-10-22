package com.opencastsoftware.energysourcechecker.controllers;
import com.opencastsoftware.energysourcechecker.exceptions.CarbonIntensityClientException;
import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import com.opencastsoftware.energysourcechecker.repositories.UserAnswerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CarbonIntensityControllerIT {

    @Autowired
    CarbonIntensityController carbonIntensityController;

    @Autowired
    UserAnswerRepository userAnswerRepository;

    @AfterEach
    public void tearDown() {
        userAnswerRepository.deleteAll();
    }

    @Test
    public void should_return_renewable_response(){
        String postcode = "SW12";
        LocalDate startDate = LocalDate.of(2024,10,1);
        LocalDate endDate = LocalDate.of(2024,10,2);

        UserAnswers user = UserAnswers.builder().postcode(postcode).startDate(startDate).endDate(endDate).build();
        userAnswerRepository.save(user);


        ResponseEntity<String> result = carbonIntensityController.getRegionalIntensity();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo("renewable");
    }

    @Test
    public void should_return_nuclear_response(){
        String postcode = "LA3";
        LocalDate startDate = LocalDate.of(2024,10,1);
        LocalDate endDate = LocalDate.of(2024,10,2);

        UserAnswers user = UserAnswers.builder().postcode(postcode).startDate(startDate).endDate(endDate).build();
        userAnswerRepository.save(user);

        ResponseEntity<String> result = carbonIntensityController.getRegionalIntensity();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo("nuclear");
    }

    @Test
    public void should_return_non_renewable_response(){
        String postcode = "LA3";
        LocalDate startDate = LocalDate.of(2024,10,5);
        LocalDate endDate = LocalDate.of(2024,10,6);

        UserAnswers user = UserAnswers.builder().postcode(postcode).startDate(startDate).endDate(endDate).build();
        userAnswerRepository.save(user);

        ResponseEntity<String> result = carbonIntensityController.getRegionalIntensity();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo("non-renewable");
    }

    @Test
    public void should_return_out_of_range_error(){
        String postcode = "LA3";
        LocalDate startDate = LocalDate.of(2024,9,1);
        LocalDate endDate = LocalDate.of(2024,9,16);

        UserAnswers user = UserAnswers.builder().postcode(postcode).startDate(startDate).endDate(endDate).build();
        userAnswerRepository.save(user);

        Exception exception = assertThrows(CarbonIntensityClientException.class, () -> {
            carbonIntensityController.getRegionalIntensity();
        });

        assertThat(exception.getMessage()).contains("The date range you have specified is greater than 14 days. Please select a smaller date range");
    }

    @Test
    public void should_return_start_date_greater_end_date_error(){
        String postcode = "LA3";
        LocalDate startDate = LocalDate.of(2024,8,15);
        LocalDate endDate = LocalDate.of(2024,8,14);

        UserAnswers user = UserAnswers.builder().postcode(postcode).startDate(startDate).endDate(endDate).build();
        userAnswerRepository.save(user);


        Exception exception = assertThrows(CarbonIntensityClientException.class, () -> {
            carbonIntensityController.getRegionalIntensity();
        });

        assertThat(exception.getMessage()).contains("The start datetime should be less than the end datetime");
    }
}
