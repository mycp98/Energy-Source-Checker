package com.opencastsoftware.energysourcechecker.controllers;

import com.opencastsoftware.energysourcechecker.services.CarbonIntensityService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log
@RestController
public class CarbonIntensityController {

    @Autowired
    private CarbonIntensityService carbonIntensityService;

    @GetMapping("/carbonIntensity")
    public ResponseEntity<String> getRegionalIntensity() {

            String response = carbonIntensityService.fetchAndProcessRegionalIntensityData();
            return ResponseEntity.ok(response);
    }
}

