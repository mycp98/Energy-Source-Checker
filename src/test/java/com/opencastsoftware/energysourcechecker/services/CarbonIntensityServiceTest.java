package com.opencastsoftware.energysourcechecker.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencastsoftware.energysourcechecker.clients.CarbonIntensityClient;
import com.opencastsoftware.energysourcechecker.exceptions.CarbonIntensityClientException;
import com.opencastsoftware.energysourcechecker.exceptions.UserAnswersException;
import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import com.opencastsoftware.energysourcechecker.repositories.UserAnswerRepository;
import com.opencastsoftware.energysourcechecker.responses.CarbonIntensityResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarbonIntensityServiceTest {
    @Mock
    CarbonIntensityClient carbonIntensityClient;

    @Mock
    UserAnswerRepository userAnswerRepository;

    @InjectMocks
    CarbonIntensityService carbonIntensityService;

    @ParameterizedTest
    @CsvSource({"RenewableResponse.json, renewable",
                "NonRenewableResponse.json, non-renewable",
                "NuclearResponse.json, nuclear"})
    public void testShouldReturnExpectedResponse(String jsonPath, String expected) throws IOException {
        String startDate = "2024-10-12";
        String endDate = "2024-10-02";
        String postcode = "SW12";

        InputStream json = CarbonIntensityResponse.class.getClassLoader().getResourceAsStream(jsonPath);

        CarbonIntensityResponse response = new ObjectMapper().readValue(json, CarbonIntensityResponse.class);

        UserAnswers user = UserAnswers.builder()
                .postcode(postcode)
                .startDate(LocalDate.parse(startDate)).endDate(LocalDate.parse(endDate)).build();

        when(userAnswerRepository.findAll()).thenReturn(List.of(user));

        when(carbonIntensityClient.getCarbonIntensityData(user.getStartDate().toString(), user.getEndDate().toString(), user.getPostcode())).thenReturn(response);

        String result = carbonIntensityService.fetchAndProcessRegionalIntensityData();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testShouldThrowUserAnswersExceptionWhenPostcodeIsMissing() {
        String startDate = "2024-10-12";
        String endDate = "2024-10-02";

        UserAnswers user = UserAnswers.builder().startDate(LocalDate.parse(startDate)).endDate(LocalDate.parse(endDate)).build();

        when(userAnswerRepository.findAll()).thenReturn(List.of(user));

        UserAnswersException exception = assertThrows(UserAnswersException.class, () -> {
            carbonIntensityService.fetchAndProcessRegionalIntensityData();
        });

        assertThat(exception.getMessage()).isEqualTo("Missing postcode");
    }

    @Test
    public void testShouldThrowUserAnswersExceptionWhenStartDateIsMissing() {
        String postcode = "SW12";
        String endDate = "2024-10-02";

        UserAnswers user = UserAnswers.builder().postcode(postcode).endDate(LocalDate.parse(endDate)).build();

        when(userAnswerRepository.findAll()).thenReturn(List.of(user));

        UserAnswersException exception = assertThrows(UserAnswersException.class, () -> {
            carbonIntensityService.fetchAndProcessRegionalIntensityData();
        });

        assertThat(exception.getMessage()).isEqualTo("Missing start date");
    }

    @Test
    public void testShouldThrowUserAnswersExceptionWhenEndDateIsMissing() {
        String postcode = "SW12";
        String startDate = "2024-10-12";

        UserAnswers user = UserAnswers.builder().postcode(postcode).startDate(LocalDate.parse(startDate)).build();

        when(userAnswerRepository.findAll()).thenReturn(List.of(user));

        UserAnswersException exception = assertThrows(UserAnswersException.class, () -> {
            carbonIntensityService.fetchAndProcessRegionalIntensityData();
        });

        assertThat(exception.getMessage()).isEqualTo("Missing end date");
    }

    @Test
    public void testShouldThrowClientExceptionWhenClientFails() {
        String startDate = "2024-10-12";
        String endDate = "2024-10-02";
        String postcode = "SW12";

        UserAnswers user = UserAnswers.builder()
                .postcode(postcode)
                .startDate(LocalDate.parse(startDate)).endDate(LocalDate.parse(endDate)).build();

        when(userAnswerRepository.findAll()).thenReturn(List.of(user));

        when(carbonIntensityClient.getCarbonIntensityData(startDate, endDate, postcode)).thenThrow(new CarbonIntensityClientException("api error"));

        CarbonIntensityClientException exception = assertThrows(CarbonIntensityClientException.class, () -> {
            carbonIntensityService.fetchAndProcessRegionalIntensityData();
        });

        assertThat(exception.getMessage()).isEqualTo("api error");

        verify(carbonIntensityClient).getCarbonIntensityData(startDate, endDate, postcode);
    }


}
