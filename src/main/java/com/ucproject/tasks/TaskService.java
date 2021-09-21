package com.ucproject.tasks;

import com.ucproject.users.models.Users;
import com.ucproject.users.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepo;

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
