package com.opencastsoftware.energysourcechecker.controllers;

import com.opencastsoftware.energysourcechecker.services.PostcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostcodeController {

    @Autowired
    private PostcodeService postcodeService;

    @PostMapping("/postcode")
    public void postcode(String postcode) {
        postcodeService.createPostcode(postcode);
    }
}
