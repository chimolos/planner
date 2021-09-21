package com.ucproject.goals.services;

import com.ucproject.goals.entity.Steps;
import com.ucproject.goals.entity.Goals;
import com.ucproject.goals.repository.StepRepository;
import com.ucproject.goals.repository.GoalRepository;
import com.ucproject.goals.request.GoalsRequest;
import com.ucproject.users.models.Users;
import com.ucproject.users.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalService {

    @Autowired
    UsersRepository usersRepo;

    @Autowired
    GoalRepository goalsRepo;

    @Autowired
    StepRepository stepRepo;

    public void addGoal(GoalsRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Goals add = new Goals();
        add.setUser(user);
        add.setTitle(request.getTitle());

        List<String> step = request.getStep();
        List<Steps> steps = new ArrayList<>();
        step.forEach(name -> {
            Steps step1 = new Steps();
            step1.setDescription(name);
            step1.setGoal(add);

            steps.add(step1);
        });

        add.getSteps().addAll(steps);
        goalsRepo.save(add);
    }

    public void updateGoal(Long id, GoalsRequest request) {
        Goals editGoal = getGoalById(id);

        editGoal.setTitle(request.getTitle());
        List<String> step = request.getStep();
        List<Steps> steps = new ArrayList<>();
        step.forEach(name -> {
            List<String> s = editGoal.getSteps().stream().map(Steps::getDescription).collect(Collectors.toList());
            if (!s.contains(name)) {
                Steps st = new Steps();
                st.setDescription(name);
                editGoal.setCompleted(false);
                st.setGoal(editGoal);

                steps.add(st);
            } else {
                Steps steps1 = stepRepo.findByGoalIdAndDescription(id, name);
                steps.add(steps1);
            }
        });

        editGoal.setSteps(steps);
        goalsRepo.save(editGoal);
    }

    public List<Goals> getAllCompletedGoals() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        return goalsRepo.findAllByUserAndCompleted(user, true);
    }

    public List<Goals> getAllActiveGoals() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        return goalsRepo.findAllByUserAndCompleted(user, false);
    }

    public Goals getGoalById(Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        return goalsRepo.findByIdAndUser(id, user);
    }

}
