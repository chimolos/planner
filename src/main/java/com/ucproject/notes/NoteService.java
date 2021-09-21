package com.ucproject.notes;

import com.ucproject.users.models.Users;
import com.ucproject.users.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    NoteRepository noteRepo;

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
