package com.opencastsoftware.energysourcechecker.services;

import com.opencastsoftware.energysourcechecker.exceptions.PostcodeException;
import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import com.opencastsoftware.energysourcechecker.repositories.UserAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PostcodeService {

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    String regex = "^[a-zA-Z]{1,2}\\d[a-zA-Z\\d]?\\s\\d[a-zA-Z]{2}$";

    boolean validatePostcode(String postcode) {
        return postcode.matches(regex);
    }

    public UserAnswers createPostcode(String postcode) throws PostcodeException {

        if (!validatePostcode(postcode)) {
            throw new PostcodeException("invalid postcode");
        }

        UserAnswers userAnswers = UserAnswers.builder().postcode(postcode.split(" ")[0]).build();
        return userAnswerRepository.save(userAnswers);
    }

}
