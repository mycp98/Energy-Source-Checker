package com.opencastsoftware.energysourcechecker.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencastsoftware.energysourcechecker.clients.CarbonIntensityClient;
import com.opencastsoftware.energysourcechecker.exceptions.CarbonIntensityClientException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarbonIntensityServiceTest {
    @Mock
    CarbonIntensityClient carbonIntensityClient;

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

        when(carbonIntensityClient.getCarbonIntensityData(startDate, endDate, postcode)).thenReturn(response);

        String result = carbonIntensityService.fetchAndProcessRegionalIntensityData(startDate, endDate, postcode);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testShouldThrowExceptionWhenClientFails() {
        String startDate = "2024-10-12";
        String endDate = "2024-10-02";
        String postcode = "SW12";

        when(carbonIntensityClient.getCarbonIntensityData(startDate, endDate, postcode)).thenThrow(new CarbonIntensityClientException("api error"));

        CarbonIntensityClientException exception = assertThrows(CarbonIntensityClientException.class, () -> {
            carbonIntensityService.fetchAndProcessRegionalIntensityData(startDate, endDate, postcode);
        });

        assertThat(exception.getMessage()).isEqualTo("api error");

        verify(carbonIntensityClient).getCarbonIntensityData(startDate, endDate, postcode);
    }
}
