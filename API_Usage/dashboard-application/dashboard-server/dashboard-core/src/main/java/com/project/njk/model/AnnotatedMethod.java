package com.project.njk.model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ekiajen on 15/06/2016.
 */
@Entity
@Table(name = "AnnotatedClass")
public class AnnotatedMethod extends AbstractPersistable<Long> implements Serializable {

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private API api;

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
