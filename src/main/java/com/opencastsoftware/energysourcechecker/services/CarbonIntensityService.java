package com.opencastsoftware.energysourcechecker.services;


import com.opencastsoftware.energysourcechecker.clients.CarbonIntensityClient;
import com.opencastsoftware.energysourcechecker.exceptions.CarbonIntensityClientException;
import com.opencastsoftware.energysourcechecker.responses.CarbonIntensityResponse;
import com.opencastsoftware.energysourcechecker.utils.DataProcessingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarbonIntensityService {

    @Autowired
    private CarbonIntensityClient carbonIntensityClient;

    public String fetchAndProcessRegionalIntensityData(String startDate, String endDate, String postcode) {

        CarbonIntensityResponse response = carbonIntensityClient.getCarbonIntensityData(startDate, endDate, postcode);
        return DataProcessingUtils.getMostEnergyProduced(response);
    }
}
