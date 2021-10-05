package com.ucproject.groups;

import com.ucproject.goals.entity.Goals;
import com.ucproject.lists.entity.Lists;
import com.ucproject.notes.Note;
import com.ucproject.tasks.Task;
import com.ucproject.teams.Team;
import com.ucproject.users.models.Users;
import com.ucproject.users.repositories.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
@Slf4j
public class GroupController {

    @Autowired
    UsersRepository usersRepo;

    @Autowired
    GroupRepository groupRepo;

    @PostMapping("/createGroup")
    public String createGroup (@RequestBody GroupRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Group newGroup = new Group();
        newGroup.setUser(user);
        newGroup.setName(request.getName());

        List<String> addUsers = request.getUsersName();
        List<Users> users = new ArrayList<>();

        addUsers.forEach(name -> {
            Users eachUser = usersRepo.findByUsername(name);
            if (eachUser == null) {
                throw new UsernameNotFoundException("User does not exist");
            } else  {
                users.add(eachUser);
            }
        });

        newGroup.setUsers(users);

        groupRepo.save(newGroup);
        return "Group created successfully";
    }

    @GetMapping("/user/groups")
    public List<Group> getGroupsPerUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        return groupRepo.findAllByUsers(user);
    }

    @GetMapping("/created/groups")
    public List<Group> getAllCreatedGroupsPerUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);
        return groupRepo.findAllByUser(user);
    }

    @GetMapping("/group/{id}")
    public Optional<Group> getTeamById(@PathVariable Long id) {
        return groupRepo.findById(id);
    }

    @PostMapping("/user/addToGroup/{groupId}")
    public ResponseEntity<?> addUserToGroup(@PathVariable Long groupId, @RequestParam("usersName") String usersName) {
        log.info("Adding user {} to group {}", usersName, groupId);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Group group = groupRepo.findByIdAndUser(groupId, user);
        Users addUser = usersRepo.findByUsername(usersName);
        if (addUser == null) {
            throw new UsernameNotFoundException("User does not exist");
        }
        group.getUsers().add(addUser);
        groupRepo.save(group);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/group/{id}/edit")
    public String editGroup(@PathVariable Long id, @RequestParam("name") String groupName) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Group group = groupRepo.findByIdAndUser(id, user);
        group.setName(groupName);
        groupRepo.save(group);

        return "Group edited successfully";
    }

    @PutMapping("/groupUser/{groupId}/remove")
    public String removeUser(@PathVariable Long groupId, @RequestParam("usersName") String usersName) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Group group = groupRepo.findByIdAndUser(groupId, user);

        Users userOut = usersRepo.findByUsername(usersName);
        group.getUsers().remove(userOut);
        groupRepo.save(group);

        return "User removed successfully";
    }

    @Transactional
    @DeleteMapping("/group/{id}/delete")
    public void deleteGroup(@PathVariable Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        groupRepo.deleteByIdAndUser(id, user);
    }

    @GetMapping("/group/{groupId}/goals/shared")
    public List<Goals> getAllSharedGoalsByGroup(@PathVariable Long groupId) {
        Group group = groupRepo.findById(groupId).get();
        return group.getSharedGoals();
    }

    @GetMapping("/group/{groupId}/lists/shared")
    public List<Lists> getAllSharedListsByGroup(@PathVariable Long groupId) {
        Group group = groupRepo.findById(groupId).get();
        return group.getSharedLists();
    }

    @GetMapping("/group/{groupId}/tasks/shared")
    public List<Task> getAllSharedTasksByGroup(@PathVariable Long groupId) {
        Group group = groupRepo.findById(groupId).get();
        return group.getSharedTasks();
    }

    @GetMapping("/group/{groupId}/notes/shared")
    public List<Note> getAllSharedNotesByGroup(@PathVariable Long groupId) {
        Group group = groupRepo.findById(groupId).get();
        return group.getSharedNotes();
    }

}
