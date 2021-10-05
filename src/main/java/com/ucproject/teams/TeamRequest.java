package com.ucproject.teams;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TeamRequest {

    @NotNull
    private String name;

    @NotNull
    private List<String> usersName;
}
