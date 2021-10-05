package com.ucproject.tasks;

import com.ucproject.users.models.Users;
import com.ucproject.users.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    TaskRepository taskRepo;

    @Autowired
    UsersRepository usersRepo;

    @PostMapping("/addTask")
    public String addTask(@RequestParam("title") String title, @RequestParam("description") String description) {
        taskService.addTask(title,description);
        return "Task added successfully";
    }

    @PutMapping("/task/{id}/edit")
    public String editTask(@PathVariable Long id, @RequestParam("title") String title, @RequestParam("description") String description) {
        taskService.updateTask(id, title, description);
        return "Task edited successfully";
    }

    @PutMapping("/task/state/{id}")
    public ResponseEntity<Task> changeCompletedState(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.changeCompletedState(id));
    }

    @GetMapping("/tasks/completed")
    public List<Task> getAllCompletedTasks() {
        return taskService.getAllCompletedTasks();
    }

    @GetMapping("/tasks/active")
    public List<Task> getAllActiveTasks() {
        return taskService.getAllActiveTasks();
    }

    @GetMapping("/task/{id}")
    public Optional<Task> getTaskById(@PathVariable Long id) {
        return taskRepo.findById(id);
    }

    @Transactional
    @DeleteMapping("/task/{id}/delete")
    public void deleteTask(@PathVariable Long id) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        taskRepo.deleteByUserAndId(user, id);
    }

    @PostMapping("/task/{id}/share")
    public String shareGoal(@PathVariable Long id, @RequestParam("classify")String classify, @RequestParam("name")String name) {
        taskService.shareTask(id, classify, name);
        return "Task shared successfully";
    }
}
