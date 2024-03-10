package com.web.usue_eer.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "user_tasks")
public class UserTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    private String status;

    private LocalDateTime dateDelivery;
    private String commentStudent;
    private String commentTeacher;

    private Integer resultScore;

    public UserTask() {
    }

    public UserTask(User user, Task task, String status, LocalDateTime dateDelivery, String commentStudent) {
        this.user = user;
        this.task = task;
        this.status = status;
        this.dateDelivery = dateDelivery;
        this.commentStudent = commentStudent;
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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDateDelivery() {
        return dateDelivery;
    }

    public void setDateDelivery(LocalDateTime dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    public Integer getResultScore() {
        return resultScore;
    }

    public void setResultScore(Integer resultScore) {
        this.resultScore = resultScore;
    }

    public String getCommentStudent() {
        return commentStudent;
    }

    public void setCommentStudent(String commentStudent) {
        this.commentStudent = commentStudent;
    }

    public String getCommentTeacher() {
        return commentTeacher;
    }

    public void setCommentTeacher(String commentTeacher) {
        this.commentTeacher = commentTeacher;
    }
}
