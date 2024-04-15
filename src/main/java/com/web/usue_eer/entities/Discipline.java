package com.web.usue_eer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Entity
@Table(name = "disciplines")
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    private boolean access;

    @OneToMany(mappedBy = "discipline", cascade = CascadeType.ALL)
    private Set<RequestDiscipline> requestDisciplines;

    public Discipline() {
    }

    public Discipline(String name, User owner, boolean access) {
        this.name = name;
        this.owner = owner;
        this.access = access;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public Set<RequestDiscipline> getRequestDisciplines() {
        return requestDisciplines;
    }

    public void setRequestDisciplines(Set<RequestDiscipline> requestDisciplines) {
        this.requestDisciplines = requestDisciplines;
    }
}
