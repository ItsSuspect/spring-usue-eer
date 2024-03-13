package com.web.usue_eer.payload.response;

public class TaskListResponse {
    //Обязательные поля
    private Long id;
    private String name;
    private Integer maxScore;
    private String formattedDateTimeDelivery;
    private String formattedDateTimeIssue;
    private String status;
    //Необязательные поля
    private int countSend;
    private int countChecked;
    private Integer resultScore;
    public TaskListResponse(Long id, String name, Integer maxScore, String formattedDateTimeDelivery, String formattedDateTimeIssue) {
        this.id = id;
        this.name = name;
        this.maxScore = maxScore;
        this.formattedDateTimeDelivery = formattedDateTimeDelivery;
        this.formattedDateTimeIssue = formattedDateTimeIssue;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCountSend() {
        return countSend;
    }

    public void setCountSend(int countSend) {
        this.countSend = countSend;
    }

    public int getCountChecked() {
        return countChecked;
    }

    public void setCountChecked(int countChecked) {
        this.countChecked = countChecked;
    }

    public Integer getResultScore() {
        return resultScore;
    }

    public void setResultScore(Integer resultScore) {
        this.resultScore = resultScore;
    }
}
