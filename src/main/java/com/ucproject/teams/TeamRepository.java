package com.ucproject.teams;

import com.ucproject.users.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface TeamRepository extends JpaRepository<Team, Long> {

//    Team findByIdAndUsers(Long id, Users user);
    List<Team> findAllByUser(Users users);
    List<Team> findAllByUsers(Users users);
    Team findByIdAndUser(Long id, Users user);
    Team findByNameAndUser(String name, Users user);
    void deleteByIdAndUser(Long id, Users user);
}
