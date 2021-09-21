package com.ucproject.goals.services;

import com.ucproject.goals.entity.Goals;
import com.ucproject.goals.entity.Steps;
import com.ucproject.goals.repository.StepRepository;
import com.ucproject.goals.repository.GoalRepository;
import com.ucproject.tasks.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StepService {

    @Autowired
    GoalRepository goalRepo;

    @Autowired
    StepRepository stepRepo;

    public void editStep(Long id, String description){
        Steps editedSteps = stepRepo.findById(id).get();

        editedSteps.setDescription(description);
        stepRepo.save(editedSteps);

    }

    public void changeCompletedState(Long id) {
        Steps step = stepRepo.findById(id).get();
        if (step != null) {
            step.setCompleted(!step.isCompleted());
            stepRepo.save(step);

            Long goalId = step.getGoal();
            Goals goal = goalRepo.findById(goalId).get();
            boolean found = false;
            List<Boolean> st = goal.getSteps().stream().map(Steps::isCompleted).collect(Collectors.toList());
            for (Boolean x : st) {
                if (x.equals(true)) {
                    found = true;
                }else {
                    found = false;
                    break;
                }
            }
            goal.setCompleted(found);
            goalRepo.save(goal);
        }
    }
}
