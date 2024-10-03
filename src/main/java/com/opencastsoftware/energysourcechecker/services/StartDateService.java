package com.opencastsoftware.energysourcechecker.services;
import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import com.opencastsoftware.energysourcechecker.repositories.UserAnswerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StartDateService {

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    public UserAnswers createStartDate(LocalDate startDate) {
        UserAnswers userAnswers = userAnswerRepository.findAll().get(0);
        userAnswers.setStartDate(startDate);
        return userAnswerRepository.save(userAnswers);
    }
}
