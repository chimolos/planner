package com.ucproject.outlines;

import com.ucproject.BaseIdModel;
import com.ucproject.users.models.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "activities")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Activity extends BaseIdModel {

    @NotBlank
    private String day;

    @NotBlank
    private String activity;

    @Temporal(TemporalType.TIME)
    private Date starttime;

    @Temporal(TemporalType.TIME)
    private Date endtime;

    @ManyToOne
    private Users user;
}
