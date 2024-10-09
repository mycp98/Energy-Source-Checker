package com.opencastsoftware.energysourcechecker.responses;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CarbonIntensityResponse {
    @JsonProperty("data")
    private CarbonIntensityResponseData data;

    @Data
    public static class CarbonIntensityResponseData {
        @JsonProperty("regionid")
        private Integer regionId;

        @JsonProperty("dnoregion")
        private String dnoRegion;

        @JsonProperty("shortname")
        private String shortName;

        @JsonProperty("postcode")
        private String postcode;

        @JsonProperty("data")
        private List<DataEntry> data;
    }

    @Data
    public static class DataEntry {
        @JsonProperty("from")
        private String from;

        @JsonProperty("to")
        private String to;

        @JsonProperty("intensity")
        private Intensity intensity;

        @JsonProperty("generationmix")
        private List<GenerationMix> generationmix;
    }

    @Data
    public static class Intensity {
        @JsonProperty("forecast")
        private Integer forecast;

        @JsonProperty("index")
        private String index;
    }

    @Data
    public static class GenerationMix {
        @JsonProperty("fuel")
        private String fuel;

        @JsonProperty("perc")
        private double perc;
    }
}




