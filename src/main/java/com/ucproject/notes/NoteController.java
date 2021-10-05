package com.ucproject.notes;

import com.ucproject.users.models.Users;
import com.ucproject.users.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class NoteController {

    @Autowired
    NoteService noteService;

    @Autowired
    NoteRepository noteRepo;

    @Autowired
    UsersRepository usersRepo;

    @PostMapping("/addNote")
    public String addNote(@RequestBody NoteRequest request) {
        noteService.addNote(request);
        return "Note added successfully";
    }

    @PutMapping("/note/{id}/edit")
    public String editNote(@PathVariable Long id, @RequestBody NoteRequest request) {
        noteService.updateNote(id, request);
        return "Note edited successfully";
    }

    @GetMapping("/notes")
    public List<Note> getAllNotes(){
        return noteService.getAllNotesPerUser();
    }

    @GetMapping("/note/{id}")
    public Optional<Note> getNoteById(@PathVariable Long id) {
        return noteRepo.findById(id);
    }

    @Transactional
    @DeleteMapping("/note/{id}/delete")
    public void deleteNote(@PathVariable Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        noteRepo.deleteByUserAndId(user, id);
    }

    @PostMapping("/note/{id}/share")
    public String shareGoal(@PathVariable Long id, @RequestParam("classify")String classify, @RequestParam("name")String name) {
        noteService.shareNote(id, classify, name);
        return "Note shared successfully";
    }
}
