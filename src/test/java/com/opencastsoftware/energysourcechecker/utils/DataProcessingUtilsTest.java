package com.opencastsoftware.energysourcechecker.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencastsoftware.energysourcechecker.responses.CarbonIntensityResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class DataProcessingUtilsTest {


    @ParameterizedTest
    @CsvSource({"RenewableResponse.json, renewable",
            "NonRenewableResponse.json, non-renewable",
            "NuclearResponse.json, nuclear"})
    public void shouldReturnExpectedResponse(String jsonPath, String expected) throws IOException {

        InputStream json = CarbonIntensityResponse.class.getClassLoader().getResourceAsStream(jsonPath);

        CarbonIntensityResponse response = new ObjectMapper().readValue(json, CarbonIntensityResponse.class);

        assertThat(DataProcessingUtils.getMostEnergyProduced(response)).isEqualTo(expected);
    }

}
