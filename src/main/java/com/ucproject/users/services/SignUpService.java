package com.ucproject.users.services;

import com.ucproject.users.models.Role;
import com.ucproject.users.models.Users;
import com.ucproject.users.repositories.UsersRepository;
import com.ucproject.users.requests.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SignUpService {

    @Autowired
    UsersService usersService;

    @Autowired
    UsersRepository usersRepo;

    @Autowired
    PasswordEncoder encoder;

    public String registerUser(SignUpRequest signUpRequest) {

        if (checkIfUsernameExists(signUpRequest.getUsername())) {
            throw new IllegalStateException("Error: Username is already taken!");
        }

        if (checkIfEmailExist(signUpRequest.getEmail())) {
            throw new IllegalStateException("Error: Email is already in use!");
        }

        Users user = new Users(signUpRequest.getEmail(),
                signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()),
                Role.USER);

        usersService.saveUser(user);

        return "User successfully registered";
    }

    public String registerAdmin(SignUpRequest signUpRequest) {

        if (checkIfUsernameExists(signUpRequest.getUsername())) {
            throw new IllegalStateException("Error: Username is already taken!");
        }

        if (checkIfEmailExist(signUpRequest.getEmail())) {
            throw new IllegalStateException("Error: Email is already in use!");
        }

        Users user = new Users(signUpRequest.getEmail(),
                signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()),
                Role.ADMIN);

        usersService.saveUser(user);

        return "Admin successfully registered";
    }

    public boolean checkIfUsernameExists(String username) {
        Users user = usersRepo.findByUsername(username);
        return user != null;
    }
    public boolean checkIfEmailExist(String email) {
        Users user = usersRepo.findByEmail(email);
        return user != null;
    }

//    public boolean checkIfUsernameEmailExists(String username, String email) {
//        Users user = usersRepo.findByUsername(username);
//        if (user!=null) {
//            if (!user.getEmail().equals(email)) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//        return false;
//    }

//    public boolean checkIfAdminEmailExists(String email, Role userRole) {
//        Users user = usersRepo.findByEmailAndUserRole(email, userRole);
//        if (user!=null){
//            return true;
//        }
//        return false;
//    }
//
//    public boolean checkIfAdminUsernameExists(String username, Role userRole) {
//        Users user = usersRepo.findByUsernameAndUserRole(username, userRole);
//        if (user!=null){
//            return true;
//        }
//        return false;
//    }
}
