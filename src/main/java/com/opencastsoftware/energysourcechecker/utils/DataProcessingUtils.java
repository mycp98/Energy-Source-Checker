package com.opencastsoftware.energysourcechecker.utils;

import com.opencastsoftware.energysourcechecker.responses.CarbonIntensityResponse;

import java.util.*;

public class DataProcessingUtils {
    public static String getMostEnergyProduced(CarbonIntensityResponse carbonIntensityResponse) {
        Set<String> renewableFuels = Set.of("biomass", "hydro", "wind", "solar");
        Set<String> nuclear = Set.of("nuclear");
        Set<String> nonRenewableFuels = Set.of("gas", "coal", "imports", "other");

        List<CarbonIntensityResponse.GenerationMix> generationMixList = carbonIntensityResponse.getData().getData().stream().flatMap(dataEntry -> dataEntry.getGenerationmix().stream()).toList();
        HashMap<String, Double> totals = new HashMap<>(Map.of
                        (
                        "renewable", 0.0,
                        "nuclear", 0.0,
                        "non-renewable", 0.0
                        ));

        for(CarbonIntensityResponse.GenerationMix generationMix : generationMixList) {
            if(renewableFuels.contains(generationMix.getFuel())) {
                totals.put("renewable", totals.get("renewable") + generationMix.getPerc());
            }
            if(nuclear.contains(generationMix.getFuel())) {
                totals.put("nuclear", totals.get("nuclear") + generationMix.getPerc());
            }
            if(nonRenewableFuels.contains(generationMix.getFuel())) {
                totals.put("non-renewable", totals.get("non-renewable") + generationMix.getPerc());
            }
        }

        return Collections.max(totals.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

}
