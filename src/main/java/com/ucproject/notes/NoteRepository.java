package com.ucproject.notes;

import com.ucproject.users.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface NoteRepository extends JpaRepository<Note, Long> {

    Note findByIdAndUser(Long id, Users users);

    List<Note> findAllByUser(Users user);

    void deleteByUserAndId(Users users, Long id);
}
