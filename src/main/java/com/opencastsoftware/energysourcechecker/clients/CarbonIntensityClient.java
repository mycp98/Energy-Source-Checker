package com.opencastsoftware.energysourcechecker.clients;

import com.opencastsoftware.energysourcechecker.exceptions.CarbonIntensityClientException;
import com.opencastsoftware.energysourcechecker.responses.CarbonIntensityResponse;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Log
@Component
public class CarbonIntensityClient {

    @Autowired
    private RestTemplate restTemplate;

    public CarbonIntensityResponse getCarbonIntensityData(String startDate, String endDate, String postcode) {
        String url = String.format("/regional/intensity/%s/%s/postcode/%s", startDate, endDate, postcode);

        try{
            return restTemplate.getForObject(url, CarbonIntensityResponse.class);
        } catch (RestClientException e){
            log.info("error is: " + e.getMessage());
            throw new CarbonIntensityClientException(e.getMessage());
        }
    }

}
