package com.opencastsoftware.energysourcechecker.controllers;

import com.opencastsoftware.energysourcechecker.exceptions.StartDateException;
import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import com.opencastsoftware.energysourcechecker.repositories.UserAnswerRepository;
import com.opencastsoftware.energysourcechecker.services.EndDateService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EndDateController.class)
public class EndDateControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserAnswerRepository userAnswerRepository;

    @SpyBean
    private EndDateService endDateService;

    @Test
    public void testPostEndDate()  throws Exception {

        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now();


        UserAnswers user = UserAnswers.builder().postcode("AB12 3CD").startDate(startDate).build();

        when(userAnswerRepository.findAll()).thenReturn(List.of(user));

        mockMvc.perform(post("/endDate").content(endDate.toString()).contentType(MediaType.TEXT_PLAIN)
        ).andExpect(status().isOk());
    }

    @Test
    public void testInvalidPostEndDateIfEndDateIsInFuture() throws Exception {

        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now().plusDays(10);


        UserAnswers user = UserAnswers.builder().postcode("AB12 3CD").startDate(startDate).build();

        when(userAnswerRepository.findAll()).thenReturn(List.of(user));

        MvcResult result = mockMvc.perform(post("/endDate").content(endDate.toString()).contentType(MediaType.TEXT_PLAIN)
        ).andExpect(status().isBadRequest()).andReturn();

        assert result.getResponse().getContentAsString().equals("EndDate is in the future");
    }

    @Test
    public void testInvalidEndDateBeforeStartDate() throws Exception {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().minusDays(10);

        UserAnswers user = UserAnswers.builder().postcode("AB12 3CD").startDate(startDate).build();

        when(userAnswerRepository.findAll()).thenReturn(List.of(user));

        MvcResult result = mockMvc.perform(post("/endDate").content(endDate.toString()).contentType(MediaType.TEXT_PLAIN)
        ).andExpect(status().isBadRequest()).andReturn();

        assert result.getResponse().getContentAsString().equals("EndDate is before StartDate");
    }

    @Test
    public void testInvalidTimeFrameMustBeFourteenDaysOrLess() throws Exception {
        LocalDate startDate = LocalDate.now().minusDays(15);
        LocalDate endDate = LocalDate.now();

        UserAnswers user = UserAnswers.builder().postcode("AB12 3CD").startDate(startDate).build();

        when(userAnswerRepository.findAll()).thenReturn(List.of(user));

        MvcResult result = mockMvc.perform(post("/endDate").content(endDate.toString()).contentType(MediaType.TEXT_PLAIN)
        ).andExpect(status().isBadRequest()).andReturn();

        assert result.getResponse().getContentAsString().equals("StartDate and EndDate are more than 14 days apart");
    }
}
