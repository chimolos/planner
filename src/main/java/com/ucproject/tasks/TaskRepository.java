package com.ucproject.tasks;

import com.ucproject.users.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findByIdAndUser(Long id, Users user);

    List<Task> findAllByUserAndCompleted(Users user, Boolean completed);

    void deleteByUserAndId(Users users, Long id);
}
