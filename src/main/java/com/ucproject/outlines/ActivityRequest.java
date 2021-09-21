package com.ucproject.outlines;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
public class ActivityRequest {

    @NotNull
    private String day;

    @NotNull
    private String activity;

//    @DateTimeFormat(pattern = "HH:mm:ss")
    private String starttime;

//    @DateTimeFormat(pattern = "HH:mm:ss")
    private String endtime;

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
}
