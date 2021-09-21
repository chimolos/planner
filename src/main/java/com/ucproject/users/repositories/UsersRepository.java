package com.ucproject.users.repositories;

import com.ucproject.users.models.Role;
import com.ucproject.users.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Repository
@Transactional
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
    Users findByEmail(String email);
//    Users findByUsernameAndUserRole(String username, Role userRole);
//    Users findByEmailAndUserRole(String email, Role userRole);

}
