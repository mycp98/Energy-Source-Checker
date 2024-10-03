package com.opencastsoftware.energysourcechecker.services;

import com.opencastsoftware.energysourcechecker.exceptions.PostcodeException;
import com.opencastsoftware.energysourcechecker.exceptions.StartDateException;
import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import com.opencastsoftware.energysourcechecker.repositories.UserAnswerRepository;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StartDateServiceTest {

    @Mock
    private UserAnswerRepository userAnswerRepository;

    @InjectMocks
    private StartDateService startDateService;

    @Test
    public void testAddsStartDateToExistingAnswers() {

        LocalDate date = LocalDate.of(2024, 10, 3);

        UserAnswers existingAnswers = UserAnswers.builder().postcode("AB12 9DE").build();
        when(userAnswerRepository.findAll()).thenReturn(List.of(existingAnswers));

        UserAnswers expectedAnswers = UserAnswers.builder().postcode("AB12 9DE").startDate(date).build();;
        when(userAnswerRepository.save(any())).thenReturn(expectedAnswers);

        UserAnswers resultingAnswers = startDateService.createStartDate(date.toString());
        assert resultingAnswers.equals(expectedAnswers);

    }

    @Test
    public void testCreateStartDateThrowsExceptionForInvalidStartDate() {
        LocalDate invalidStartDate = LocalDate.now().plusDays(10);

        StartDateException exception = assertThrows(StartDateException.class, () -> {
            startDateService.createStartDate(invalidStartDate.toString());
        });

        assertThat(exception.getMessage()).isEqualTo("start date is in the future");
    }

    @Test
    public void testShouldReturnFalseIfStartDateIsInFuture(){
        LocalDate futureDate = LocalDate.now().plusDays(10);
        assertThat(startDateService.validateStartDate(futureDate)).isFalse();
    }

    @Test
    public void testShouldReturnTrueIfStartDateIsInPastOrPresent(){
        LocalDate futureDate = LocalDate.now().minusDays(10);
        assertThat(startDateService.validateStartDate(futureDate)).isTrue();
    }

}
