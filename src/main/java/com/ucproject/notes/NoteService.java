package com.ucproject.notes;

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
public class NoteService {

    @Autowired
    NoteRepository noteRepo;

    @Autowired
    TeamRepository teamRepo;

    @Autowired
    GroupRepository groupRepo;

    @Autowired
    FamilyRepository familyRepo;

    @Autowired
    UsersRepository usersRepo;

    public void addNote(NoteRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Note note = new Note();
        note.setTitle(request.getTitle());
        note.setDescription(request.getDescription());
        note.setContent(request.getContent());
        note.setUser(user);

        noteRepo.save(note);
    }

    public void updateNote(Long id, NoteRequest request) {
        Note editNote = getNoteById(id);

        editNote.setTitle(request.getTitle());
        editNote.setDescription(request.getDescription());
        editNote.setContent(request.getContent());

        noteRepo.save(editNote);
    }

    public void shareNote(Long id, String classify, String name) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Note shareNote = getNoteById(id);

        switch (classify) {
            case "user":
                Users users = usersRepo.findByEmail(name);
                if (users == null) {
                    log.error("User not found in the database");
                    throw new UsernameNotFoundException("User not found in the database");
                }
                users.getSharedNotes().add(shareNote);
                usersRepo.save(users);

                break;
            case "team":
                Team team = teamRepo.findByNameAndUser(name, user);
                if (team == null) {
                    log.error("Team not found in the database");
                    throw new UsernameNotFoundException("Team not found in the database");
                }
                team.getSharedNotes().add(shareNote);
                teamRepo.save(team);

                break;
            case "group":
                Group group = groupRepo.findByNameAndUser(name, user);
                if (group == null) {
                    log.error("Group not found in the database");
                    throw new UsernameNotFoundException("Group not found in the database");
                }
                group.getSharedNotes().add(shareNote);
                groupRepo.save(group);

                break;
            case "family":
                Family family = familyRepo.findByNameAndUser(name, user);
                if (family == null) {
                    log.error("Family not found in the database");
                    throw new UsernameNotFoundException("Family not found in the database");
                }
                family.getSharedNotes().add(shareNote);
                familyRepo.save(family);

                break;
        }

    }

    public Note getNoteById(Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        return noteRepo.findByIdAndUser(id, user);
    }

    public List<Note> getAllNotesPerUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        return noteRepo.findAllByUser(user);
    }
}
