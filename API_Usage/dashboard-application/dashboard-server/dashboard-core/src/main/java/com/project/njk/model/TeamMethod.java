package com.project.njk.model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by ekiajen on 15/06/2016.
 */
@Entity
@Table(name = "TeamMethod")
public class TeamMethod extends AbstractPersistable<Long> implements Serializable {

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AnnotatedClass> annotatedClasses;

    @ManyToMany
    @JoinTable(
            name = "TeamMethod_AnnotatedMethod",
            joinColumns = @JoinColumn(name = "Team_Method_Id", referencedColumnName = "ID"))
    private List<AnnotatedMethod> annotatedMethods;

    @ManyToOne(fetch = FetchType.LAZY)
    private TeamClass teamClass;

    public void setName(String name) {
        this.name = name;
    }

    public void setAnnotatedClasses(List<AnnotatedClass> annotatedClasses) {
        this.annotatedClasses = annotatedClasses;
    }

    public void setAnnotatedMethods(List<AnnotatedMethod> annotatedMethods) {
        this.annotatedMethods = annotatedMethods;
    }

    public String getName() {
        return name;

    }

    public List<AnnotatedClass> getAnnotatedClasses() {
        return annotatedClasses;
    }

    public List<AnnotatedMethod> getAnnotatedMethods() {
        return annotatedMethods;
    }
}
