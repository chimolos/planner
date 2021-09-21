package com.ucproject.lists.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ucproject.BaseIdModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "items")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Item extends BaseIdModel {

    @NotBlank
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("steps")
    private Lists list;

    public Long getList() {
        return list.getId();
    }

    public void setList(Lists list) {
        this.list = list;
    }
}
