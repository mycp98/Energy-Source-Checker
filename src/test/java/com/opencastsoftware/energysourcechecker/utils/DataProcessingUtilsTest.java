package com.opencastsoftware.energysourcechecker.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencastsoftware.energysourcechecker.responses.CarbonIntensityResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class DataProcessingUtilsTest {


    @Test
    public void shouldReturnRenewable() throws IOException {

        InputStream json = CarbonIntensityResponse.class.getClassLoader().getResourceAsStream("RenewableResponse.json");

        CarbonIntensityResponse renewableResponse = new ObjectMapper().readValue(json, CarbonIntensityResponse.class);

        assertThat(DataProcessingUtils.getMostEnergyProduced(renewableResponse)).isEqualTo("renewable");
    }

    @Test
    public void shouldReturnNuclear() throws IOException {

        InputStream json = CarbonIntensityResponse.class.getClassLoader().getResourceAsStream("NuclearResponse.json");

        CarbonIntensityResponse nuclearResponse = new ObjectMapper().readValue(json, CarbonIntensityResponse.class);

        assertThat(DataProcessingUtils.getMostEnergyProduced(nuclearResponse)).isEqualTo("nuclear");
    }

    @Test
    public void shouldReturnNonRenewable() throws IOException {

        InputStream json = CarbonIntensityResponse.class.getClassLoader().getResourceAsStream("NonRenewableResponse.json");

        CarbonIntensityResponse nonRenewableResponse = new ObjectMapper().readValue(json, CarbonIntensityResponse.class);

        assertThat(DataProcessingUtils.getMostEnergyProduced(nonRenewableResponse)).isEqualTo("non-renewable");
    }
}
