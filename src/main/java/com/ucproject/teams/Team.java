package com.ucproject.teams;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ucproject.BaseIdModel;
import com.ucproject.goals.entity.Goals;
import com.ucproject.lists.entity.Lists;
import com.ucproject.notes.Note;
import com.ucproject.tasks.Task;
import com.ucproject.users.models.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity(name = "teams")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Team extends BaseIdModel{

    @NotBlank
    private String name;

    @ManyToOne
    private Users user;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Users> users;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Goals> sharedGoals;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Lists> sharedLists;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Task> sharedTasks;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Note> sharedNotes;
}
