package com.ucproject.teams;

import com.ucproject.goals.entity.Goals;
import com.ucproject.lists.entity.Lists;
import com.ucproject.notes.Note;
import com.ucproject.tasks.Task;
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
public class TeamController {

    @Autowired
    UsersRepository usersRepo;

    @Autowired
    TeamRepository teamRepo;

    @PostMapping("/createTeam")
    public String createTeam (@RequestBody TeamRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Team newTeam = new Team();
        newTeam.setUser(user);
        newTeam.setName(request.getName());

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

        newTeam.setUsers(users);

        teamRepo.save(newTeam);
        return "Team created successfully";
    }

    @GetMapping("/user/teams")
    public List<Team> getTeamsPerUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);
        return teamRepo.findAllByUsers(user);
    }

    @GetMapping("/created/teams")
    public List<Team> getAllCreatedTeamsPerUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);
        return teamRepo.findAllByUser(user);
    }

    @GetMapping("/team/{id}")
    public Optional<Team> getTeamById(@PathVariable Long id) {
        return teamRepo.findById(id);
    }

    @PostMapping("/user/addToTeam/{teamId}")
    public ResponseEntity<?> addUserToTeam(@PathVariable Long teamId, @RequestParam("usersName") String usersName) {
        log.info("Adding user {} to team {}", usersName, teamId);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Team team = teamRepo.findByIdAndUser(teamId, user);
        Users addUser = usersRepo.findByUsername(usersName);
        if (addUser == null) {
            throw new UsernameNotFoundException("User does not exist");
        }
        team.getUsers().add(addUser);
        teamRepo.save(team);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/team/{id}/edit")
    public String editTeam(@PathVariable Long id, @RequestParam("name") String teamName) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Team team = teamRepo.findByIdAndUser(id, user);
        team.setName(teamName);
        teamRepo.save(team);

        return "Team edited successfully";
    }

    @PutMapping("/teamUser/{teamId}/remove")
    public String removeUser(@PathVariable Long teamId, @RequestParam("usersName") String usersName) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Team team = teamRepo.findByIdAndUser(teamId, user);

        Users userOut = usersRepo.findByUsername(usersName);
        team.getUsers().remove(userOut);
        teamRepo.save(team);

        return "User removed successfully";
    }

    @Transactional
    @DeleteMapping("/team/{id}/delete")
    public void deleteTeam(@PathVariable Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        teamRepo.deleteByIdAndUser(id, user);
    }

    @GetMapping("/team/{teamId}/goals/shared")
    public List<Goals> getAllSharedGoalsByTeam(@PathVariable Long teamId) {
        Team team = teamRepo.findById(teamId).get();
        return team.getSharedGoals();
    }

    @GetMapping("/team/{teamId}/lists/shared")
    public List<Lists> getAllSharedListsByTeam(@PathVariable Long teamId) {
        Team team = teamRepo.findById(teamId).get();
        return team.getSharedLists();
    }

    @GetMapping("/team/{teamId}/tasks/shared")
    public List<Task> getAllSharedTasksByTeam(@PathVariable Long teamId) {
        Team team = teamRepo.findById(teamId).get();
        return team.getSharedTasks();
    }

    @GetMapping("/team/{teamId}/notes/shared")
    public List<Note> getAllSharedNotesByTeam(@PathVariable Long teamId) {
        Team team = teamRepo.findById(teamId).get();
        return team.getSharedNotes();
    }

}
