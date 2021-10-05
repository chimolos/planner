package com.ucproject.tasks;

import com.ucproject.family.Family;
import com.ucproject.family.FamilyRepository;
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

import java.util.List;

@Service
@Slf4j
public class TaskService {

    @Autowired
    TaskRepository taskRepo;

    @Autowired
    TeamRepository teamRepo;

    @Autowired
    GroupRepository groupRepo;

    @Autowired
    FamilyRepository familyRepo;

    @Autowired
    UsersRepository usersRepo;

    public void addTask(String title, String description) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setUser(user);

        taskRepo.save(task);
    }

    public void updateTask(Long id, String title, String description) {
        Task editTask = getTaskById(id);

        editTask.setTitle(title);
        editTask.setDescription(description);

        taskRepo.save(editTask);
    }

    public void shareTask(Long id, String classify, String name) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Task sharedTask = getTaskById(id);

        switch (classify) {
            case "user":
                Users users = usersRepo.findByEmail(name);
                if (users == null) {
                    log.error("User not found in the database");
                    throw new UsernameNotFoundException("User not found in the database");
                }
                users.getSharedTasks().add(sharedTask);
                usersRepo.save(users);

                break;
            case "team":
                Team team = teamRepo.findByNameAndUser(name, user);
                if (team == null) {
                    log.error("Team not found in the database");
                    throw new UsernameNotFoundException("Team not found in the database");
                }
                team.getSharedTasks().add(sharedTask);
                teamRepo.save(team);

                break;
            case "group":
                Group group = groupRepo.findByNameAndUser(name, user);
                if (group == null) {
                    log.error("Group not found in the database");
                    throw new UsernameNotFoundException("Group not found in the database");
                }
                group.getSharedTasks().add(sharedTask);
                groupRepo.save(group);

                break;
            case "family":
                Family family = familyRepo.findByNameAndUser(name, user);
                if (family == null) {
                    log.error("Family not found in the database");
                    throw new UsernameNotFoundException("Family not found in the database");
                }
                family.getSharedTasks().add(sharedTask);
                familyRepo.save(family);

                break;
        }

    }

    public Task changeCompletedState(Long id) {
        Task task = getTaskById(id);
        if (task != null) {
            task.setCompleted(!task.isCompleted());
            taskRepo.save(task);
            return task;
        }
        return null;
    }

    public List<Task> getAllCompletedTasks() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        return taskRepo.findAllByUserAndCompleted(user, true);
    }

    public List<Task> getAllActiveTasks() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        return taskRepo.findAllByUserAndCompleted(user, false);
    }

    public Task getTaskById(Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        return taskRepo.findByIdAndUser(id, user);
    }
}
