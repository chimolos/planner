package com.ucproject.goals.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ucproject.BaseIdModel;
import com.ucproject.users.models.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "goals")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Goals extends BaseIdModel {

    @NotBlank
    private String title;

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("goal")
    private List<Steps> steps = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @CreationTimestamp
    @Column(name = "created_date")
    private Timestamp createdDate;

    private boolean completed;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private Timestamp dateCompleted;

    public Timestamp getDateCompleted() {
        if (isCompleted()) {
            return dateCompleted;
        }
        return null;
    }

    public void setDateCompleted(Timestamp dateCompleted) {
            this.dateCompleted = dateCompleted;
    }

}
