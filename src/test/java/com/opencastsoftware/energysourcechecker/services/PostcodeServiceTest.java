package com.opencastsoftware.energysourcechecker.services;

import com.opencastsoftware.energysourcechecker.exceptions.PostcodeException;
import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import com.opencastsoftware.energysourcechecker.repositories.UserAnswerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostcodeServiceTest {

    @Mock
    private UserAnswerRepository userAnswerRepository;

    @InjectMocks
    private PostcodeService postcodeService;

    @Test
    public void testCreateUser() {

        UserAnswers user = UserAnswers.builder().postcode("AB12").build();

        when(userAnswerRepository.save(user)).thenReturn(user);

        UserAnswers result = postcodeService.createPostcode("AB12 9DE");

        assert result.equals(user);
    }

    @Test
    public void testCreatePostcodeThrowsExceptionForInvalidPostcode() {
        String invalidPostcode = "ABCD EFG";

        PostcodeException exception = assertThrows(PostcodeException.class, () -> {
            postcodeService.createPostcode(invalidPostcode);
        });

        assertThat(exception.getMessage()).isEqualTo("invalid postcode");
    }

    @Test
    public void testValidPostcode(){
        boolean result = postcodeService.validatePostcode("AB12 9DE");
        assertThat(result).isTrue();
    }

    @Test
    public void testInvalidPostcode(){
        boolean result = postcodeService.validatePostcode("123456");
        assertThat(result).isFalse();
    }

}
