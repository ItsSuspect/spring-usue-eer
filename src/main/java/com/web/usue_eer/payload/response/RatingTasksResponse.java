package com.web.usue_eer.payload.response;

public class RatingTasksResponse {
    private Long taskId;
    private String taskName;
    private Integer resultScore;
    private Integer maxScore;

    public RatingTasksResponse(Long taskId, String taskName, Integer resultScore, Integer maxScore) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.resultScore = resultScore;
        this.maxScore = maxScore;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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
}
