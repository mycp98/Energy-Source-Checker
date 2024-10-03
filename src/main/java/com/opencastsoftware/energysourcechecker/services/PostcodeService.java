package com.opencastsoftware.energysourcechecker.services;

import com.opencastsoftware.energysourcechecker.exceptions.PostcodeException;
import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import com.opencastsoftware.energysourcechecker.repositories.UserAnswerRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PostcodeService {

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    String regex = "^[a-zA-Z]{1,2}\\d[a-zA-Z\\d]?\\s*\\d[a-zA-Z]{2}$";

    public void validatePostcode(String postcode) throws PostcodeException{
        if (!postcode.matches(regex)) {
            throw new PostcodeException("Invalid postcode format");
        }
    }

    public UserAnswers createPostcode(String postcode) {

        UserAnswers userAnswers = UserAnswers.builder().postcode(postcode).build();
        return userAnswerRepository.save(userAnswers);
    }

}
