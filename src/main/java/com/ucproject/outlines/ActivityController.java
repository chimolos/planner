package com.ucproject.outlines;

import com.ucproject.users.models.Users;
import com.ucproject.users.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
public class ActivityController {

    @Autowired
    ActivityService activityService;

    @Autowired
    ActivityRepository activityRepo;

    @Autowired
    UsersRepository usersRepo;

    @PostMapping("/addActivity")
    public String addActivity(@RequestBody ActivityRequest request) throws ParseException {
        activityService.addActivity(request);
        return "New activity added";
    }

    @PutMapping("/activity/{id}/edit")
    public String editActivity(@PathVariable Long id, @RequestBody ActivityRequest request) throws ParseException {
        activityService.updateActivity(request, id);
        return "Activity updated successfully";
    }

    @GetMapping("/activity/{id}")
    public Activity getActivityById(@PathVariable Long id) {
        return activityService.getActivityById(id);
    }

    @GetMapping("/activities/{day}")
    public List<Activity> getAllActivitiesByDay(@PathVariable String day) {
        return activityService.getAllByDay(day);
    }

    @DeleteMapping("/activity/{id}/delete")
    public void deleteActivity(@PathVariable  Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        activityRepo.deleteByUserAndId(user, id);
    }
}
