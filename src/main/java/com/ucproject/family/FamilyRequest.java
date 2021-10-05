package com.ucproject.family;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FamilyRequest {

    @NotNull
    private String name;

    @NotNull
    private List<String> usersName;
}
