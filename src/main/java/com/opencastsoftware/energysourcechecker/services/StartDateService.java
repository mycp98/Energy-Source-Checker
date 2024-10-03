package com.opencastsoftware.energysourcechecker.services;
import com.opencastsoftware.energysourcechecker.exceptions.StartDateException;
import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import com.opencastsoftware.energysourcechecker.repositories.UserAnswerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StartDateService {

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    boolean validateStartDate(LocalDate startDate){
        return !startDate.isAfter(LocalDate.now());
    }

    public UserAnswers createStartDate(String startDate) {

        //TODO exception handling
        LocalDate date = LocalDate.parse(startDate);

        if (!validateStartDate(date)){
            throw new StartDateException("start date is in the future");
        }

        //TODO unhandled exception
        UserAnswers userAnswers = userAnswerRepository.findAll().get(0);
        userAnswers.setStartDate(date);
        return userAnswerRepository.save(userAnswers);
    }
}
