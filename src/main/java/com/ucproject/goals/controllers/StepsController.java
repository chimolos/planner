package com.ucproject.goals.controllers;

import com.ucproject.goals.entity.Steps;
import com.ucproject.goals.entity.Goals;
import com.ucproject.goals.repository.StepRepository;
import com.ucproject.goals.services.StepService;
import com.ucproject.goals.services.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
public class StepsController {

    @Autowired
    StepService stepService;

    @Autowired
    GoalService goalService;

    @Autowired
    StepRepository stepRepo;

    @PutMapping("/step/{id}/edit")
    public String editItem(@PathVariable Long id, @RequestParam("description") String description) {
        stepService.editStep(id, description);
        return "Step successfully edited";
    }

    @PutMapping("/step/state/{id}")
    public void changeCompletedState(@PathVariable Long id){
        stepService.changeCompletedState(id);
    }

    @Transactional
    @DeleteMapping("/{goalId}/step/{id}/delete")
    public void deleteItem(@PathVariable Long goalId, @PathVariable Long id) {
        Goals goal = goalService.getGoalById(goalId);
        Steps steps = stepRepo.findByGoalAndId(goal, id);
        goal.getSteps().remove(steps);
        stepRepo.delete(steps);
    }
}
