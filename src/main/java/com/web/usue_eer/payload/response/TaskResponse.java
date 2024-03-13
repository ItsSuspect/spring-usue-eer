package com.web.usue_eer.payload.response;

import com.web.usue_eer.entities.User;

import java.time.LocalDateTime;

public class TaskResponse {
    private Long id;
    private String instruction;
    private String name;
    private Integer resultScore;
    private Integer maxScore;
    private String formattedDateTimeDelivery;
    private String formattedDateTimeIssue;
    private String dateDelivery;
    private String status;
    private String commentTeacher;
    private String commentStudent;
    private User user;
    private boolean sending;

    public TaskResponse(Long id, String instruction, String name, String status, Integer maxScore, String formattedDateTimeDelivery, String formattedDateTimeIssue, User user, boolean sending) {
        this.id = id;
        this.instruction = instruction;
        this.name = name;
        this.status = status;
        this.maxScore = maxScore;
        this.formattedDateTimeDelivery = formattedDateTimeDelivery;
        this.formattedDateTimeIssue = formattedDateTimeIssue;
        this.user = user;
        this.sending = sending;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
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

    public String getFormattedDateTimeIssue() {
        return formattedDateTimeIssue;
    }

    public void setFormattedDateTimeIssue(String formattedDateTimeIssue) {
        this.formattedDateTimeIssue = formattedDateTimeIssue;
    }

    public String getFormattedDateTimeDelivery() {
        return formattedDateTimeDelivery;
    }

    public void setFormattedDateTimeDelivery(String formattedDateTimeDelivery) {
        this.formattedDateTimeDelivery = formattedDateTimeDelivery;
    }

    public String getDateDelivery() {
        return dateDelivery;
    }

    public void setDateDelivery(String dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCommentTeacher() {
        return commentTeacher;
    }

    public void setCommentTeacher(String commentTeacher) {
        this.commentTeacher = commentTeacher;
    }

    public String getCommentStudent() {
        return commentStudent;
    }

    public void setCommentStudent(String commentStudent) {
        this.commentStudent = commentStudent;
    }

    public Integer getResultScore() {
        return resultScore;
    }

    public void setResultScore(Integer resultScore) {
        this.resultScore = resultScore;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSending() {
        return sending;
    }

    public void setSending(boolean sending) {
        this.sending = sending;
    }
}