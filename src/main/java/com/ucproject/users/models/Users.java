package com.ucproject.users.models;

import com.ucproject.BaseIdModel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
@EqualsAndHashCode
@RequiredArgsConstructor
public class Users extends BaseIdModel {

    @NotBlank
    @Email
//    @Column(unique = true)
    private String email;

    @NotBlank
//    @Column(unique = true)
    private String username;

    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    private Role userRole;

    public Users(String email, String username, String password, Role userRole) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }
}
