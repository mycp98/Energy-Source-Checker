package com.opencastsoftware.energysourcechecker.services;

import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import com.opencastsoftware.energysourcechecker.repositories.UserAnswerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostcodeServiceTest {

    @Mock
    private UserAnswerRepository userAnswerRepository;

    @InjectMocks
    private PostcodeService postcodeService;

    @Test
    public void testCreateUser(){

        UserAnswers user = UserAnswers.builder().postcode("AB12 CDE").build();

        when(userAnswerRepository.save(user)).thenReturn(user);

        UserAnswers result = postcodeService.createPostcode("AB12 CDE");

        assert result.equals(user);
    }

}
