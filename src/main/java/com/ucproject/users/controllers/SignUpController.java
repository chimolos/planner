package com.ucproject.users.controllers;

import com.ucproject.users.requests.SignUpRequest;
import com.ucproject.users.services.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/signup")
public class SignUpController {

    @Autowired
    SignUpService signUpService;

    @PostMapping("/user")
    public String registerUser(@RequestBody @Valid SignUpRequest signUpRequest) {
        return signUpService.registerUser(signUpRequest);
    }

    @PostMapping("/admin")
    public String registerAdmin(@RequestBody SignUpRequest signUpRequest) {
        return signUpService.registerAdmin(signUpRequest);
    }
}
