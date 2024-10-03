package com.opencastsoftware.energysourcechecker.repositories;

import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class UserAnswerRepositoryIntegrationTest {

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Test
    public void testCreateUserAnswers(){
        UserAnswers initial = new UserAnswers(1L, "NE12 9PG", LocalDate.now());
        UserAnswers result = userAnswerRepository.save(initial);
        assert initial.equals(result);
    }

}
