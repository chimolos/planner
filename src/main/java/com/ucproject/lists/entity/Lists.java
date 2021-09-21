package com.ucproject.lists.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ucproject.BaseIdModel;
import com.ucproject.users.models.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lists")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Lists extends BaseIdModel {

    @NotBlank
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("goal")
    private List<Item> item = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private Users user;

    @UpdateTimestamp
    @Column(name = "last_modified_date")
    private Timestamp lastModifiedDate;


}
