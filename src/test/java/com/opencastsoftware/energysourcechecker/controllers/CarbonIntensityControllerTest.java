package com.opencastsoftware.energysourcechecker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencastsoftware.energysourcechecker.clients.CarbonIntensityClient;
import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import com.opencastsoftware.energysourcechecker.repositories.UserAnswerRepository;
import com.opencastsoftware.energysourcechecker.responses.CarbonIntensityResponse;
import com.opencastsoftware.energysourcechecker.services.CarbonIntensityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CarbonIntensityController.class)
public class CarbonIntensityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserAnswerRepository userAnswerRepository;

    @MockBean
    CarbonIntensityClient carbonIntensityClient;

    @SpyBean
    CarbonIntensityService carbonIntensityService;

    @ParameterizedTest
    @CsvSource({"RenewableResponse.json, renewable",
            "NonRenewableResponse.json, non-renewable",
            "NuclearResponse.json, nuclear"})
    public void testGetCarbonIntensity(String jsonPath, String expected) throws Exception {
        String startDate = "2024-10-12";
        String endDate = "2024-10-02";
        String postcode = "SW12";

        InputStream json = CarbonIntensityResponse.class.getClassLoader().getResourceAsStream(jsonPath);

        CarbonIntensityResponse response = new ObjectMapper().readValue(json, CarbonIntensityResponse.class);

        UserAnswers user = UserAnswers.builder()
                .postcode(postcode)
                .startDate(LocalDate.parse(startDate)).endDate(LocalDate.parse(endDate)).build();

        when(userAnswerRepository.findAll()).thenReturn(List.of(user));

        when(carbonIntensityClient.getCarbonIntensityData(user.getStartDate().toString(), user.getEndDate().toString(), user.getPostcode())).thenReturn(response);

        mockMvc.perform(get("/carbonIntensity"))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    public void testPostcodeIsNull() throws Exception {
        String startDate = "2024-10-12";
        String endDate = "2024-10-02";

        UserAnswers user = UserAnswers.builder().startDate(LocalDate.parse(startDate)).endDate(LocalDate.parse(endDate)).build();

        when(userAnswerRepository.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/carbonIntensity"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing postcode"));
    }

    @Test
    public void testStartDateIsNull() throws Exception {
        String endDate = "2024-10-02";
        String postcode = "SW12";

        UserAnswers user = UserAnswers.builder().postcode(postcode).endDate(LocalDate.parse(endDate)).build();

        when(userAnswerRepository.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/carbonIntensity"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing start date"));
    }

    @Test
    public void testEndDateIsNull() throws Exception {
        String startDate = "2024-10-12";
        String postcode = "SW12";

        UserAnswers user = UserAnswers.builder().postcode(postcode).startDate(LocalDate.parse(startDate)).build();

        when(userAnswerRepository.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/carbonIntensity"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing end date"));
    }
}
