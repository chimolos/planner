package com.ucproject.tasks;

import com.ucproject.BaseIdModel;
import com.ucproject.users.models.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Task extends BaseIdModel {

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    private boolean completed;

    @UpdateTimestamp
    private Timestamp lastUpdatedTime;
}
