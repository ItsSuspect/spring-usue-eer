package com.web.usue_eer.payload.request;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TaskRequest {
    private String name;
    private Integer maxScore;
    private LocalDate dateIssue;
    private LocalTime timeIssue;
    private LocalDate dateDelivery;
    private LocalTime timeDelivery;
    private String instructionTask;

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

    public LocalDate getDateIssue() {
        return dateIssue;
    }

    public void setDateIssue(LocalDate dateIssue) {
        this.dateIssue = dateIssue;
    }

    public LocalTime getTimeIssue() {
        return timeIssue;
    }

    public void setTimeIssue(LocalTime timeIssue) {
        this.timeIssue = timeIssue;
    }

    public LocalDate getDateDelivery() {
        return dateDelivery;
    }

    public void setDateDelivery(LocalDate dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    public LocalTime getTimeDelivery() {
        return timeDelivery;
    }

    public void setTimeDelivery(LocalTime timeDelivery) {
        this.timeDelivery = timeDelivery;
    }

    public String getInstructionTask() {
        return instructionTask;
    }

    public void setInstructionTask(String instructionTask) {
        this.instructionTask = instructionTask;
    }
}
