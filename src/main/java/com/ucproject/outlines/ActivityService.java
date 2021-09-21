package com.ucproject.outlines;

import com.ucproject.users.models.Users;
import com.ucproject.users.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class ActivityService {

    @Autowired
    ActivityRepository activityRepo;

    @Autowired
    UsersRepository usersRepo;

    public void addActivity(ActivityRequest request) throws ParseException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Activity newActivity = new Activity();
        newActivity.setUser(user);
        newActivity.setDay(request.getDay());
        newActivity.setActivity(request.getActivity());
        newActivity.setStarttime(new SimpleDateFormat("HH:mm").parse(request.getStarttime()));
        newActivity.setEndtime(new SimpleDateFormat("HH:mm").parse(request.getEndtime()));

        activityRepo.save(newActivity);
    }

    public void updateActivity(ActivityRequest request, Long id) throws ParseException {
        Activity editedActivity = getActivityById(id);

        editedActivity.setActivity(request.getActivity());
        editedActivity.setDay(request.getDay());
        editedActivity.setStarttime(new SimpleDateFormat("HH:mm").parse(request.getStarttime()));
        editedActivity.setEndtime(new SimpleDateFormat("HH:mm").parse(request.getEndtime()));

        activityRepo.save(editedActivity);

    }

    public Activity getActivityById(Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        return activityRepo.findByUserAndId(user, id);
    }

    public List<Activity> getAllByDay(String day) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        return activityRepo.findAllByUserAndDayIgnoreCase(user, day);
    }
}
