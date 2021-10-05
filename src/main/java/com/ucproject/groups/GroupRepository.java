package com.ucproject.groups;

import com.ucproject.teams.Team;
import com.ucproject.users.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findAllByUser(Users users);
    List<Group> findAllByUsers(Users users);
    Group findByNameAndUser(String name, Users user);
    Group findByIdAndUser(Long id, Users user);
    void deleteByIdAndUser(Long id, Users user);
}
