package com.ucproject.goals.services;

import com.ucproject.family.Family;
import com.ucproject.family.FamilyRepository;
import com.ucproject.goals.entity.Steps;
import com.ucproject.goals.entity.Goals;
import com.ucproject.goals.repository.StepRepository;
import com.ucproject.goals.repository.GoalRepository;
import com.ucproject.goals.request.GoalsRequest;
import com.ucproject.groups.Group;
import com.ucproject.groups.GroupRepository;
import com.ucproject.teams.Team;
import com.ucproject.teams.TeamRepository;
import com.ucproject.users.models.Users;
import com.ucproject.users.repositories.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GoalService {

    @Autowired
    UsersRepository usersRepo;

    @Autowired
    TeamRepository teamRepo;

    @Autowired
    GroupRepository groupRepo;

    @Autowired
    FamilyRepository familyRepo;

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

    public void shareGoal(Long id, String classify, String name) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Goals sharedGoal = getGoalById(id);

        switch (classify) {
            case "user":
                Users users = usersRepo.findByEmail(name);
                if (users == null) {
                    log.error("User not found in the database");
                    throw new UsernameNotFoundException("User not found in the database");
                }
                users.getSharedGoals().add(sharedGoal);
                usersRepo.save(users);

                break;
            case "team":
                Team team = teamRepo.findByNameAndUser(name, user);
                if (team == null) {
                    log.error("Team not found in the database");
                    throw new UsernameNotFoundException("Team not found in the database");
                }
                team.getSharedGoals().add(sharedGoal);
                teamRepo.save(team);

                break;
            case "group":
                Group group = groupRepo.findByNameAndUser(name, user);
                if (group == null) {
                    log.error("Group not found in the database");
                    throw new UsernameNotFoundException("Group not found in the database");
                }
                group.getSharedGoals().add(sharedGoal);
                groupRepo.save(group);

                break;
            case "family":
                Family family = familyRepo.findByNameAndUser(name, user);
                if (family == null) {
                    log.error("Family not found in the database");
                    throw new UsernameNotFoundException("Family not found in the database");
                }
                family.getSharedGoals().add(sharedGoal);
                familyRepo.save(family);

                break;
        }

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
