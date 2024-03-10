package com.web.usue_eer.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer maxScore;

    private LocalDateTime dateTimeIssue;

    private LocalDateTime dateTimeDelivery;

    @Transient
    private String formattedDateTimeIssue;

    @Transient
    private String formattedDateTimeDelivery;

    private String instruction;
    private String status;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @OneToMany(mappedBy = "task")
    private Set<UserTask> userTasks = new HashSet<>();

    public Task() {
    }

    public Task(String name, Integer maxScore, LocalDateTime dateTimeIssue, LocalDateTime dateTimeDelivery, String instruction, Discipline discipline, String status) {
        this.name = name;
        this.maxScore = maxScore;
        this.dateTimeIssue = dateTimeIssue;
        this.dateTimeDelivery = dateTimeDelivery;
        this.instruction = instruction;
        this.discipline = discipline;
        this.status = status;
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

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public LocalDateTime getDateTimeIssue() {
        return dateTimeIssue;
    }

    public void setDateTimeIssue(LocalDateTime dateTimeIssue) {
        this.dateTimeIssue = dateTimeIssue;
    }

    public LocalDateTime getDateTimeDelivery() {
        return dateTimeDelivery;
    }

    public void setDateTimeDelivery(LocalDateTime dateTimeDelivery) {
        this.dateTimeDelivery = dateTimeDelivery;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Set<UserTask> getUserTasks() {
        return userTasks;
    }

    public void setUserTasks(Set<UserTask> userTasks) {
        this.userTasks = userTasks;
    }

    public String getFormattedDateTimeIssue() {
        return formattedDateTimeIssue;
    }

    public String getFormattedDateTimeDelivery() {
        return formattedDateTimeDelivery;
    }

    public void setFormattedDateTimeIssue(String formattedDateTimeIssue) {
        this.formattedDateTimeIssue = formattedDateTimeIssue;
    }

    public void setFormattedDateTimeDelivery(String formattedDateTimeDelivery) {
        this.formattedDateTimeDelivery = formattedDateTimeDelivery;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
