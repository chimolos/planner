package com.ucproject.goals.repository;

import com.ucproject.goals.entity.Goals;
import com.ucproject.users.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface GoalRepository extends JpaRepository<Goals, Long> {

    List<Goals> findAllByUserAndCompleted(Users user, Boolean completed);
    Goals findByIdAndUser(Long id, Users user);
    void deleteByUserAndId(Users users, Long id);

}
