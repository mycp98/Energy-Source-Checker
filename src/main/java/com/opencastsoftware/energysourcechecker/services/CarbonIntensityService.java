package com.opencastsoftware.energysourcechecker.services;


import com.opencastsoftware.energysourcechecker.clients.CarbonIntensityClient;
import com.opencastsoftware.energysourcechecker.exceptions.UserAnswersException;
import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import com.opencastsoftware.energysourcechecker.repositories.UserAnswerRepository;
import com.opencastsoftware.energysourcechecker.responses.CarbonIntensityResponse;
import com.opencastsoftware.energysourcechecker.utils.DataProcessingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarbonIntensityService {

    @Autowired
    private CarbonIntensityClient carbonIntensityClient;

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    public String fetchAndProcessRegionalIntensityData() {

        UserAnswers user = userAnswerRepository.findAll().get(0);

        if (user.getPostcode() == null || user.getPostcode().isEmpty()) {
            throw new UserAnswersException("Missing postcode");
        }

        if (user.getStartDate() == null) {
            throw new UserAnswersException("Missing start date");
        }

        if (user.getEndDate() == null) {
            throw new UserAnswersException("Missing end date");
        }

        CarbonIntensityResponse response = carbonIntensityClient.getCarbonIntensityData(
                user.getStartDate().toString(),
                user.getEndDate().toString(),
                user.getPostcode()
        );

        return DataProcessingUtils.getMostEnergyProduced(response);
    }
}
