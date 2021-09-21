package com.ucproject.notes;

import com.ucproject.BaseIdModel;
import com.ucproject.users.models.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "notes")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Note extends BaseIdModel {

    private String title;

    private String description;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private Users user;

    @UpdateTimestamp
    private Timestamp lastUpdatedTime;
}
