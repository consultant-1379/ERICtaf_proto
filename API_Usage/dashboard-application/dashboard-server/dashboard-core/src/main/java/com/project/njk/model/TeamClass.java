package com.project.njk.model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by ekiajen on 15/06/2016.
 */
@Entity
@Table(name = "TeamClass")
public class TeamClass extends AbstractPersistable<Long> implements Serializable {

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TeamMethod> methods;

    @ManyToOne(fetch = FetchType.LAZY)
    private TeamJar teamJar;

    public List<TeamMethod> getTeamMethods() {
        return methods;
    }

    public void setTeamMethods(List<TeamMethod> methods) {
        this.methods = methods;
    }
}
