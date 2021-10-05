package com.ucproject.family;

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
public class FamilyController {

    @Autowired
    UsersRepository usersRepo;

    @Autowired
    FamilyRepository familyRepo;

    @PostMapping("/createFamily")
    public String createFamily (@RequestBody FamilyRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Family newFamily = new Family();
        newFamily.setUser(user);
        newFamily.setName(request.getName());

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

        newFamily.setUsers(users);

        familyRepo.save(newFamily);
        return "Family created successfully";
    }

    @GetMapping("/user/family")
    public Family getFamilyPerUser () {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);
        return familyRepo.findAllByUsers(user);
    }

    @GetMapping("/created/family")
    public Family getAllCreatedFamilyPerUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);
        return familyRepo.findAllByUser(user);
    }

    @GetMapping("/family/{id}")
    public Optional<Family> getFamilyById(@PathVariable Long id) {
        return familyRepo.findById(id);
    }

    @PostMapping("/user/addToFamily/{familyId}")
    public ResponseEntity<?> addUserToFamily(@PathVariable Long familyId, @RequestParam("usersName") String usersName) {
        log.info("Adding user {} to family {}", usersName, familyId);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Family family = familyRepo.findByIdAndUser(familyId, user);
        Users addUser = usersRepo.findByUsername(usersName);
        if (addUser == null) {
            throw new UsernameNotFoundException("User does not exist");
        }
        family.getUsers().add(addUser);
        familyRepo.save(family);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/family/{id}/edit")
    public String editFamily(@PathVariable Long id, @RequestParam("name") String familyName) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Family family = familyRepo.findByIdAndUser(id, user);
        family.setName(familyName);
        familyRepo.save(family);

        return "Family edited successfully";
    }

    @PutMapping("/familyUser/{familyId}/remove")
    public String removeUser(@PathVariable Long familyId, @RequestParam("usersName") String usersName) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Family family = familyRepo.findByIdAndUser(familyId, user);

        Users userOut = usersRepo.findByUsername(usersName);
        family.getUsers().remove(userOut);
        familyRepo.save(family);

        return "User removed successfully";
    }

    @Transactional
    @DeleteMapping("/family/{id}/delete")
    public void deleteFamily(@PathVariable Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        familyRepo.deleteByIdAndUser(id, user);
    }

    @GetMapping("/family/{familyId}/goals/shared")
    public List<Goals> getAllSharedGoalsByFamily(@PathVariable Long familyId) {
        Family family = familyRepo.findById(familyId).get();
        return family.getSharedGoals();
    }

    @GetMapping("/family/{familyId}/lists/shared")
    public List<Lists> getAllSharedListsByFamily(@PathVariable Long familyId) {
        Family family = familyRepo.findById(familyId).get();
        return family.getSharedLists();
    }

    @GetMapping("/family/{familyId}/tasks/shared")
    public List<Task> getAllSharedTasksByFamily(@PathVariable Long familyId) {
        Family family = familyRepo.findById(familyId).get();
        return family.getSharedTasks();
    }

    @GetMapping("/family/{familyId}/notes/shared")
    public List<Note> getAllSharedNotesByFamily(@PathVariable Long familyId) {
        Family family = familyRepo.findById(familyId).get();
        return family.getSharedNotes();
    }

}
