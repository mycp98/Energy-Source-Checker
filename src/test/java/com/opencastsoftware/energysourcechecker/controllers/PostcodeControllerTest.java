package com.opencastsoftware.energysourcechecker.controllers;


import com.opencastsoftware.energysourcechecker.exceptions.PostcodeException;
import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import com.opencastsoftware.energysourcechecker.services.PostcodeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PostcodeController.class)
class PostcodeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private PostcodeService postcodeService;

    @Test
    public void testPostPostcode() throws Exception {

        String postcode = "AB12 5DE";

        UserAnswers user = UserAnswers.builder().postcode(postcode).build();

        when(postcodeService.createPostcode(eq(postcode))).thenReturn(user);

        mockMvc.perform(post("/postcode").content(postcode).contentType(MediaType.TEXT_PLAIN)
        ).andExpect(status().isOk());

    }

    @Test
    public void testPostInvalidPostcode() throws Exception {

        String invalidPostcode = "AB12 CDE";

        UserAnswers user = UserAnswers.builder().postcode(invalidPostcode).build();

        doThrow(new PostcodeException("postcode is invalid")).when(postcodeService).createPostcode(invalidPostcode);

        MvcResult result = mockMvc.perform(post("/postcode").content(invalidPostcode).contentType(MediaType.TEXT_PLAIN)
        ).andExpect(status().isBadRequest()).andReturn();

        assert result.getResponse().getContentAsString().equals("postcode is invalid");

    }


}