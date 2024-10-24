package com.opencastsoftware.energysourcechecker.controllers;

import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import com.opencastsoftware.energysourcechecker.repositories.UserAnswerRepository;
import com.opencastsoftware.energysourcechecker.services.StartDateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StartDateController.class)
public class StartDateControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserAnswerRepository userAnswerRepository;

    @SpyBean
    private StartDateService startDateService;

    @Test
    public void testPostStartDate()  throws Exception {

        LocalDate startDate = LocalDate.now().minusDays(10);

        UserAnswers user = UserAnswers.builder().postcode("AB12 3CD").build();
        UserAnswers userWithStartDate = UserAnswers.builder().postcode("AB12 3CD").startDate(startDate).build();

        when(userAnswerRepository.findAll()).thenReturn(List.of(user));
        when(userAnswerRepository.save(userWithStartDate)).thenReturn(userWithStartDate);

        mockMvc.perform(post("/startDate").content(startDate.toString()).contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("StartDate saved successfully"));
    }

    @Test
    public void testInvalidPostStartDate() throws Exception {

        LocalDate startDate = LocalDate.now().plusDays(10);


        MvcResult result = mockMvc.perform(post("/startDate").content(startDate.toString()).contentType(MediaType.TEXT_PLAIN)
        ).andExpect(status().isBadRequest()).andReturn();

        assert result.getResponse().getContentAsString().equals("start date is in the future");
    }
}
