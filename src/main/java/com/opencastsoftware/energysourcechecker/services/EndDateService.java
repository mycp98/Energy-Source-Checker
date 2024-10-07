package com.opencastsoftware.energysourcechecker.services;

import com.opencastsoftware.energysourcechecker.exceptions.EndDateException;
import com.opencastsoftware.energysourcechecker.models.EndDateValidation;
import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import com.opencastsoftware.energysourcechecker.repositories.UserAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EndDateService {

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    EndDateValidation validateEndDate(LocalDate startDate, LocalDate endDate) {
        if (endDate.isAfter(LocalDate.now())) {
            return EndDateValidation.ERROR_FUTURE_DATE;
        }
        if (endDate.isBefore(startDate)) {
            return EndDateValidation.ERROR_BEFORE_START_DATE;
        }
        if (startDate.plusDays(14).isBefore(endDate)) {
            return EndDateValidation.ERROR_MORE_THAN_14_DAYS;
        }
        return EndDateValidation.VALID;
    }

    public UserAnswers createEndDate(String endDate) {

        //TODO exception handling
        LocalDate date = LocalDate.parse(endDate);

        UserAnswers userAnswers = userAnswerRepository.findAll().get(0);

       EndDateValidation validationResult = validateEndDate(userAnswers.getStartDate(), date);

        switch (validationResult) {
            case ERROR_FUTURE_DATE:
                throw new EndDateException("EndDate is in the future");
            case ERROR_BEFORE_START_DATE:
                throw new EndDateException("EndDate is before StartDate");
            case ERROR_MORE_THAN_14_DAYS:
                throw new EndDateException("StartDate and EndDate are more than 14 days apart");
            case VALID:
                break;
        }

        //TODO unhandled exception
        userAnswers.setStartDate(date);
        return userAnswerRepository.save(userAnswers);
    }
}
