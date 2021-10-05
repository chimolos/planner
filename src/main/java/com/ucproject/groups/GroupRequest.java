package com.ucproject.groups;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class GroupRequest {

    @NotNull
    private String name;

    @NotNull
    private List<String> usersName;
}
