package com.web.usue_eer.payload.response;

import com.web.usue_eer.entities.User;

public class TaskCheckResponse {
    private Long id;
    private String name;
    private Integer resultScore;
    private Integer maxScore;
    private String commentStudent;
    private String commentTeacher;
    private String instruction;
    private String formattedDateTimeDelivery;
    private String formattedDateTimeIssue;
    private String dateDelivery;
    private User user;

    public TaskCheckResponse(Long id, String name, Integer resultScore, Integer maxScore, String commentStudent, String commentTeacher, String instruction, String formattedDateTimeDelivery, String formattedDateTimeIssue, String dateDelivery, User user) {
        this.id = id;
        this.name = name;
        this.resultScore = resultScore;
        this.maxScore = maxScore;
        this.commentStudent = commentStudent;
        this.commentTeacher = commentTeacher;
        this.instruction = instruction;
        this.formattedDateTimeDelivery = formattedDateTimeDelivery;
        this.formattedDateTimeIssue = formattedDateTimeIssue;
        this.dateDelivery = dateDelivery;
        this.user = user;
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

    public Integer getResultScore() {
        return resultScore;
    }

    public void setResultScore(Integer resultScore) {
        this.resultScore = resultScore;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
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

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getFormattedDateTimeDelivery() {
        return formattedDateTimeDelivery;
    }

    public void setFormattedDateTimeDelivery(String formattedDateTimeDelivery) {
        this.formattedDateTimeDelivery = formattedDateTimeDelivery;
    }

    public String getFormattedDateTimeIssue() {
        return formattedDateTimeIssue;
    }

    public void setFormattedDateTimeIssue(String formattedDateTimeIssue) {
        this.formattedDateTimeIssue = formattedDateTimeIssue;
    }

    public String getDateDelivery() {
        return dateDelivery;
    }

    public void setDateDelivery(String dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
