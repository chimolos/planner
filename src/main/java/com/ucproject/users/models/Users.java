package com.ucproject.users.models;

import com.ucproject.BaseIdModel;
import com.ucproject.goals.entity.Goals;
import com.ucproject.lists.entity.Lists;
import com.ucproject.notes.Note;
import com.ucproject.tasks.Task;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "users")
@EqualsAndHashCode
@RequiredArgsConstructor
public class Users extends BaseIdModel {

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    private Role userRole;

    @ManyToMany
    private List<Goals> sharedGoals;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Lists> sharedLists;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Task> sharedTasks;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Note> sharedNotes;

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

    public List<Goals> getSharedGoals() {
        return sharedGoals;
    }

    public void setSharedGoals(List<Goals> sharedGoals) {
        this.sharedGoals = sharedGoals;
    }

    public List<Lists> getSharedLists() {
        return sharedLists;
    }

    public void setSharedLists(List<Lists> sharedLists) {
        this.sharedLists = sharedLists;
    }

    public List<Task> getSharedTasks() {
        return sharedTasks;
    }

    public void setSharedTasks(List<Task> sharedTasks) {
        this.sharedTasks = sharedTasks;
    }

    public List<Note> getSharedNotes() {
        return sharedNotes;
    }

    public void setSharedNotes(List<Note> sharedNotes) {
        this.sharedNotes = sharedNotes;
    }
}
