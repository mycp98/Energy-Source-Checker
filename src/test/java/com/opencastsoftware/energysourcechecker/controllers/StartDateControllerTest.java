package com.opencastsoftware.energysourcechecker.controllers;

import com.opencastsoftware.energysourcechecker.exceptions.PostcodeException;
import com.opencastsoftware.energysourcechecker.exceptions.StartDateException;
import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import com.opencastsoftware.energysourcechecker.services.StartDateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StartDateController.class)
public class StartDateControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private StartDateService startDateService;

    @Test
    public void testPostStartDate()  throws Exception {

        LocalDate startDate = LocalDate.now().minusDays(10);

        UserAnswers user = UserAnswers.builder().startDate(startDate).build();

        when(startDateService.createStartDate(eq(startDate.toString()))).thenReturn(user);

        mockMvc.perform(post("/startDate").content(startDate.toString()).contentType(MediaType.TEXT_PLAIN)
        ).andExpect(status().isOk());
    }

    @Test
    public void testInvalidPostStartDate() throws Exception {

        LocalDate startDate = LocalDate.now().plusDays(10);

        doThrow(new StartDateException("start date is in the future")).when(startDateService).createStartDate(startDate.toString());
        
        MvcResult result = mockMvc.perform(post("/startDate").content(startDate.toString()).contentType(MediaType.TEXT_PLAIN)
        ).andExpect(status().isBadRequest()).andReturn();

        assert result.getResponse().getContentAsString().equals("start date is in the future");
    }
}
