package com.project.njk.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by ekiajen on 15/06/2016.
 */
@Entity
@Table(name = "AnnotatedClass")
public class AnnotatedClass extends AbstractPersistable<Long> implements Serializable {

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private API api;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<TeamClass> teamClasses;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<TeamMethod> teamMethods;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setApi(API api) {
        this.api = api;
    }

    public API getApi() {
        return api;
    }
}
