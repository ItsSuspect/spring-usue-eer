package com.web.usue_eer.payload.response;

import java.util.List;

public class RatingResponse {
    private Long disciplineId;
    private String nameDiscipline;
    private Integer rating;
    private List<RatingTasksResponse> tasks;

    public RatingResponse(Long disciplineId, String nameDiscipline, List<RatingTasksResponse> tasks, Integer rating) {
        this.disciplineId = disciplineId;
        this.nameDiscipline = nameDiscipline;
        this.tasks = tasks;
        this.rating = rating;
    }

    public Long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(Long disciplineId) {
        this.disciplineId = disciplineId;
    }

    public String getNameDiscipline() {
        return nameDiscipline;
    }

    public void setNameDiscipline(String nameDiscipline) {
        this.nameDiscipline = nameDiscipline;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public List<RatingTasksResponse> getTasks() {
        return tasks;
    }

    public void setTasks(List<RatingTasksResponse> tasks) {
        this.tasks = tasks;
    }
}
