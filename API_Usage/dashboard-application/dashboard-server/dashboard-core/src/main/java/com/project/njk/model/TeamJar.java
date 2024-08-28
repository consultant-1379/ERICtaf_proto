package com.project.njk.model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by ekiajen on 15/06/2016.
 */
@Entity
@Table(name = "TeamJar")
public class TeamJar extends AbstractPersistable<Long> implements Serializable {

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "teamJar")
    private List<TeamClass> teamClasses;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;

    }
}
