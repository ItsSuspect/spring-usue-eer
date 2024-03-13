package com.web.usue_eer.payload.response;

import com.web.usue_eer.entities.User;

public class CompletedTaskResponse {
    private Long id;
    private User user;
    private String status;
    private Integer resultScore;
    private Integer maxScore;
    private String dateDelivery;

    public CompletedTaskResponse(Long id, User user, String status, Integer resultScore, Integer maxScore, String dateDelivery) {
        this.id = id;
        this.user = user;
        this.status = status;
        this.resultScore = resultScore;
        this.maxScore = maxScore;
        this.dateDelivery = dateDelivery;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getDateDelivery() {
        return dateDelivery;
    }

    public void setDateDelivery(String dateDelivery) {
        this.dateDelivery = dateDelivery;
    }
}
