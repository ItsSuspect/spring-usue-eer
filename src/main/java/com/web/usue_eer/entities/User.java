package com.web.usue_eer.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.web.usue_eer.entities.enums.AccessType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @Email
    private String email;

    private String name;

    private String surname;

    private String middleName;

    @Transient
    private String accessType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_groups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserDiscipline> userDisciplines = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserTask> userTasks = new HashSet<>();

    public User() {
    }

    public User(String username, String password, String email, String name, String surname, String middleName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public Set<UserDiscipline> getUserDisciplines() {
        return userDisciplines;
    }

    public void setUserDisciplines(Set<UserDiscipline> userDisciplines) {
        this.userDisciplines = userDisciplines;
    }

    public Set<UserTask> getUserTasks() {
        return userTasks;
    }

    public void setUserTasks(Set<UserTask> userTasks) {
        this.userTasks = userTasks;
    }

    public List<Discipline> getDisciplines() {
        List<Discipline> disciplines = new ArrayList<>();
        for (UserDiscipline userDiscipline : userDisciplines) {
            disciplines.add(userDiscipline.getDiscipline());
        }
        return disciplines;
    }

}
