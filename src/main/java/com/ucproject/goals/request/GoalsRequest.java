package com.ucproject.goals.request;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class GoalsRequest {
    @NotBlank
    private String title;

    private List<String> step;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getStep() {
        return step;
    }

    public void setStep(List<String> step) {
        this.step = step;
    }
}
