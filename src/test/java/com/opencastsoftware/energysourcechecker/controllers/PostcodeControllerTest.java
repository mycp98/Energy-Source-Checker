package com.opencastsoftware.energysourcechecker.controllers;


import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import com.opencastsoftware.energysourcechecker.services.PostcodeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostcodeControllerTest {

    @Mock
    private PostcodeService postcodeService;

    @InjectMocks
    private PostcodeController postcodeController;

    @Test
    public void testPostPostcode()  {

        UserAnswers user = UserAnswers.builder().postcode("AB12 CDE").build();

        when(postcodeService.createPostcode(eq("AB12 CDE"))).thenReturn(user);

       postcodeController.postcode("AB12 CDE");

       verify(postcodeService).createPostcode(eq("AB12 CDE"));

    }


}