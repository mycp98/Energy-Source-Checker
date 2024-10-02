package com.opencastsoftware.energysourcechecker.services;

import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import com.opencastsoftware.energysourcechecker.repositories.UserAnswerRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostcodeService {

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    public UserAnswers createPostcode(String postcode) {
        UserAnswers userAnswers = UserAnswers.builder().postcode(postcode).build();
        return userAnswerRepository.save(userAnswers);
    }

}
