package com.ucproject.goals.repository;

import com.ucproject.goals.entity.Goals;
import com.ucproject.goals.entity.Steps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface StepRepository extends JpaRepository<Steps, Long> {
    Steps findByGoalIdAndDescription(Long goalId, String description);

    Steps findByGoalAndId(Goals goals, Long id);
}
