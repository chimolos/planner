package com.ucproject.users.services;

import com.ucproject.users.models.Users;
import com.ucproject.users.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UsersServiceImpl implements UsersService, UserDetailsService {

    private final UsersRepository usersRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepo.findByUsername(username);
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", username);
            GrantedAuthority authority = new SimpleGrantedAuthority(user.getUserRole().name());
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Arrays.asList(authority));
        }
    }

    @Override
    public void saveUser(Users user) {
        usersRepo.save(user);
    }

    @Override
    public Users getUser(String username) {
        return usersRepo.findByUsername(username);
    }
}
