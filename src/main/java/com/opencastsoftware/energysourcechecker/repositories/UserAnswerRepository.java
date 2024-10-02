package com.opencastsoftware.energysourcechecker.repositories;

import com.opencastsoftware.energysourcechecker.models.UserAnswers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswers, Long> {
}
