package com.opencastsoftware.energysourcechecker.controllers;

import com.opencastsoftware.energysourcechecker.services.PostcodeService;
import com.opencastsoftware.energysourcechecker.services.StartDateService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Log
@RestController
public class StartDateController {

    @Autowired
    private StartDateService startDateService;

    @PostMapping("/startDate")
    public ResponseEntity<String> startDate(@RequestBody String startDate) {
        log.info("startDate is " + startDate);

        startDateService.createStartDate(startDate);
        return ResponseEntity.ok("StartDate saved successfully");
    }


}
