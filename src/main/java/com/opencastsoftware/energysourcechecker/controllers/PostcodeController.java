package com.opencastsoftware.energysourcechecker.controllers;

import com.opencastsoftware.energysourcechecker.services.PostcodeService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
public class PostcodeController {

    @Autowired
    private PostcodeService postcodeService;

    @PostMapping("/postcode")
    public ResponseEntity<String> postcode(@RequestBody String postcode) {

        log.info("Postcode entered is " + postcode);

        postcodeService.createPostcode(postcode);

        return ResponseEntity.ok("Postcode saved successfully");
    }
}
