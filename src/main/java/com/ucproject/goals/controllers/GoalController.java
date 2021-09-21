package com.ucproject.goals.controllers;

import com.ucproject.goals.entity.Goals;
import com.ucproject.goals.repository.GoalRepository;
import com.ucproject.goals.request.GoalsRequest;
import com.ucproject.goals.services.GoalService;
import com.ucproject.users.models.Users;
import com.ucproject.users.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
public class GoalController {

    @Autowired
    GoalService goalService;

    @Autowired
    GoalRepository goalsRepo;

    @Autowired
    UsersRepository usersRepo;

    @PostMapping("/addGoal")
    public String addList(@RequestBody GoalsRequest request) {
        goalService.addGoal(request);
        return "Goal added successfully";
    }

    @GetMapping("/goals/completed")
    public List<Goals> getAllCompletedGoals() {
        return goalService.getAllCompletedGoals();
    }

    @GetMapping("/goals/active")
    public List<Goals> getAllActiveGoals() {
        return goalService.getAllActiveGoals();
    }

    @GetMapping("/goal/{id}")
    public Goals getGoalById(@PathVariable Long id) {
        return goalService.getGoalById(id);
    }

    @PutMapping("/goal/{id}/edit")
    public String editGoal(@PathVariable Long id, @RequestBody GoalsRequest request) {
        goalService.updateGoal(id, request);
        return "Goal successfully updated";
    }

    @Transactional
    @DeleteMapping("/goal/{id}/delete")
    public void deleteGoal(@PathVariable Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        goalsRepo.deleteByUserAndId(user, id);
    }
}
