package com.opencastsoftware.energysourcechecker.services;

import com.opencastsoftware.energysourcechecker.exceptions.EndDateException;
import com.opencastsoftware.energysourcechecker.models.EndDateValidation;
import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import com.opencastsoftware.energysourcechecker.repositories.UserAnswerRepository;
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
public class EndDateServiceTest {

    @Mock
    private UserAnswerRepository userAnswerRepository;

    @InjectMocks
    private EndDateService endDateService;

    String postcode = "AB12 9DE";

    @Test
    public void testAddsEndDateToExistingAnswers() {

        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now();

        UserAnswers existingAnswers = UserAnswers.builder().postcode(postcode).startDate(startDate).build();
        when(userAnswerRepository.findAll()).thenReturn(List.of(existingAnswers));

        UserAnswers expectedAnswers = UserAnswers.builder().postcode(postcode).startDate(startDate).endDate(endDate).build();;
        when(userAnswerRepository.save(any())).thenReturn(expectedAnswers);

        UserAnswers resultingAnswers = endDateService.createEndDate(endDate.toString());
        assert resultingAnswers.equals(expectedAnswers);
    }

    @Test
    public void testCreateEndDateThrowsExceptionIfEndDateIsInFuture() {

        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate invalidEndDate = LocalDate.now().plusDays(10);

        UserAnswers existingAnswers = UserAnswers.builder().postcode(postcode).startDate(startDate).build();
        when(userAnswerRepository.findAll()).thenReturn(List.of(existingAnswers));



        EndDateException exception = assertThrows(EndDateException.class, () -> {
            endDateService.createEndDate(invalidEndDate.toString());
        });

        assertThat(exception.getMessage()).isEqualTo("EndDate is in the future");
    }

    @Test
    public void testCreateEndDateThrowsExceptionIfEndDateIsBeforeStartDate() {

        LocalDate startDate = LocalDate.now();
        LocalDate invalidEndDate = LocalDate.now().minusDays(10);

        UserAnswers existingAnswers = UserAnswers.builder().postcode(postcode).startDate(startDate).build();
        when(userAnswerRepository.findAll()).thenReturn(List.of(existingAnswers));

        EndDateException exception = assertThrows(EndDateException.class, () -> {
            endDateService.createEndDate(invalidEndDate.toString());
        });

        assertThat(exception.getMessage()).isEqualTo("EndDate is before StartDate");
    }
    @Test
    public void testCreateEndDateThrowsExceptionIf14DaysApart() {

        LocalDate startDate = LocalDate.now().minusDays(15);
        LocalDate invalidEndDate = LocalDate.now();

        UserAnswers existingAnswers = UserAnswers.builder().postcode(postcode).startDate(startDate).build();
        when(userAnswerRepository.findAll()).thenReturn(List.of(existingAnswers));

        EndDateException exception = assertThrows(EndDateException.class, () -> {
            endDateService.createEndDate(invalidEndDate.toString());
        });

        assertThat(exception.getMessage()).isEqualTo("StartDate and EndDate are more than 14 days apart");
    }


    @Test
    public void testShouldThrowErrorIfEndDateIsInFuture(){
        LocalDate futureEndDate = LocalDate.now().plusDays(10);
        EndDateValidation result = endDateService.validateEndDate(LocalDate.now().minusDays(10), futureEndDate);
        assertThat(result).isEqualTo(EndDateValidation.ERROR_FUTURE_DATE);
    }

    @Test
    public void testShouldThrowErrorIfEndDateIsBeforeStartDate(){
        LocalDate endDate = LocalDate.now().minusDays(5);
        EndDateValidation result = endDateService.validateEndDate(LocalDate.now(), endDate);
        assertThat(result).isEqualTo(EndDateValidation.ERROR_BEFORE_START_DATE);
    }

    @Test
    public void testShouldThrowErrorIfStartDateAndEndDateAre14DaysApart(){
        LocalDate startDate = LocalDate.now().minusDays(15);
        EndDateValidation result = endDateService.validateEndDate(startDate, LocalDate.now());
        assertThat(result).isEqualTo(EndDateValidation.ERROR_MORE_THAN_14_DAYS);
    }

    @Test
    public void testShouldReturnTrueIfStartDateIsInPastOrPresent(){
        LocalDate startDate = LocalDate.now().minusDays(10);
        EndDateValidation result = endDateService.validateEndDate(startDate, LocalDate.now());
        assertThat(result).isEqualTo(EndDateValidation.VALID);
    }
}
