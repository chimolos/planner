package com.ucproject.goals.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ucproject.BaseIdModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "steps")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Steps extends BaseIdModel {

    @NotBlank
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("steps")
    private Goals goal;

    private boolean completed = false;

    public Long getGoal() {
        return goal.getId();
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this==o) return true;
        if (!(o instanceof Steps)) return false;
        return id != null && id.equals(((Steps) o).getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

