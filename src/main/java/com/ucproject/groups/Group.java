package com.ucproject.groups;

import com.ucproject.BaseIdModel;
import com.ucproject.goals.entity.Goals;
import com.ucproject.lists.entity.Lists;
import com.ucproject.notes.Note;
import com.ucproject.tasks.Task;
import com.ucproject.users.models.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity(name = "groups")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Group extends BaseIdModel{

    @NotBlank
    private String name;

    @ManyToOne
    private Users user;

    @ManyToMany
    private List<Users> users;

    @ManyToMany
    private List<Goals> sharedGoals;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Lists> sharedLists;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Task> sharedTasks;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Note> sharedNotes;
}
