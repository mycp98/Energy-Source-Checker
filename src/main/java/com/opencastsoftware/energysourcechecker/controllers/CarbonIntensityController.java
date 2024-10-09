package com.opencastsoftware.energysourcechecker.controllers;

import com.opencastsoftware.energysourcechecker.responses.CarbonIntensityResponse;
import com.opencastsoftware.energysourcechecker.services.CarbonIntensityService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log
@RestController
public class CarbonIntensityController {

    @Autowired
    private CarbonIntensityService carbonIntensityService;



    @GetMapping("/regional/intensity/{startDate}/{endDate}/postcode/{postcode}")
    public ResponseEntity<String> getRegionalIntensity(
            @PathVariable String startDate,
            @PathVariable String endDate,
            @PathVariable String postcode) {

            String response = carbonIntensityService.fetchAndProcessRegionalIntensityData(startDate, endDate, postcode);
            return ResponseEntity.ok(response);
    }
}

