package com.ucproject.family;

import com.ucproject.users.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FamilyRepository extends JpaRepository<Family, Long> {

    Family findAllByUser(Users users);
    Family findAllByUsers(Users users);
    Family findByNameAndUser(String name, Users user);
    Family findByIdAndUser(Long id, Users user);
    void deleteByIdAndUser(Long id, Users user);
}
