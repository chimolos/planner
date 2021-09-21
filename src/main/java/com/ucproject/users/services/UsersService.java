package com.ucproject.users.services;

import com.ucproject.users.models.Users;

public interface UsersService {
    void saveUser (Users user);
    Users getUser(String username);
}
