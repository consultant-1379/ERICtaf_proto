package com.project.njk.model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by ekiajen on 15/06/2016.
 */
@Entity
@Table(name = "API")
public class API extends AbstractPersistable<Long> implements Serializable {

    @Column(name = "value", nullable = false)
    private String value;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AnnotatedClass> tafClasses;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AnnotatedMethod> tafMethods;

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
