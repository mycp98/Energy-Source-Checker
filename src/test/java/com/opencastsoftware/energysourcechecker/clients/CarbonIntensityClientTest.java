package com.opencastsoftware.energysourcechecker.clients;

import com.opencastsoftware.energysourcechecker.exceptions.CarbonIntensityClientException;
import com.opencastsoftware.energysourcechecker.responses.CarbonIntensityResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarbonIntensityClientTest {

    @Mock
    RestTemplate restTemplate;
    @InjectMocks
    CarbonIntensityClient carbonIntensityClient;

    @Test
    public void testShouldGetCarbonIntensityData() {
        String startDate = "2024-10-01";
        String endDate = "2024-10-02";
        String postcode = "SW12";
        String url = String.format("/regional/intensity/%s/%s/postcode/%s", startDate, endDate, postcode);

        CarbonIntensityResponse carbonIntensityResponse = new CarbonIntensityResponse();

        when(restTemplate.getForObject(url, CarbonIntensityResponse.class)).thenReturn(carbonIntensityResponse);

        CarbonIntensityResponse actualResponse = carbonIntensityClient.getCarbonIntensityData(startDate, endDate, postcode);

        assertThat(actualResponse).isEqualTo(carbonIntensityResponse);

        verify(restTemplate).getForObject(url, CarbonIntensityResponse.class);
    }

    @Test
    public void testShouldThrowException() {
        String startDate = "2024-10-12";
        String endDate = "2024-10-02";
        String postcode = "SW12";
        String url = String.format("/regional/intensity/%s/%s/postcode/%s", startDate, endDate, postcode);

        when(restTemplate.getForObject(url, CarbonIntensityResponse.class)).thenThrow(new CarbonIntensityClientException("api error"));

        CarbonIntensityClientException exception = assertThrows(CarbonIntensityClientException.class, () -> {
            carbonIntensityClient.getCarbonIntensityData(startDate, endDate, postcode);
        });

        assertThat(exception.getMessage()).isEqualTo("api error");

        verify(restTemplate).getForObject(url, CarbonIntensityResponse.class);
    }
}
