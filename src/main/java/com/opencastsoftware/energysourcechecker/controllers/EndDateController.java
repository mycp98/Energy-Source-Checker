package com.opencastsoftware.energysourcechecker.controllers;

import com.opencastsoftware.energysourcechecker.services.EndDateService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
public class EndDateController {

    @Autowired
    private EndDateService endDateService;

    @PostMapping("/endDate")
    public ResponseEntity<String> endDate(@RequestBody String endDate) {
        log.info("endDate is " + endDate);

        endDateService.createEndDate(endDate);
        return ResponseEntity.ok("EndDate saved successfully");
    }
}
