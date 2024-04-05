package com.web.usue_eer.entities;

import com.web.usue_eer.entities.enums.AccessType;
import jakarta.persistence.*;

@Entity
@Table(name = "user_disciplines")
public class UserDiscipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @Enumerated(EnumType.STRING)
    private AccessType accessType;

    public UserDiscipline() {
    }

    public UserDiscipline(User user, Discipline discipline, AccessType accessType) {
        this.user = user;
        this.discipline = discipline;
        this.accessType = accessType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }
}
