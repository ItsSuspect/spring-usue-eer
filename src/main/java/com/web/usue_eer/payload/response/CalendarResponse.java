package com.web.usue_eer.payload.response;

public class CalendarResponse {
    private Long disciplineId;
    private Long taskId;
    private String disciplineName;
    private String taskName;
    private String dateTask;
    private String timeTask;

    public CalendarResponse(Long disciplineId, Long taskId, String disciplineName, String taskName, String dateTask, String timeTask) {
        this.disciplineId = disciplineId;
        this.taskId = taskId;
        this.disciplineName = disciplineName;
        this.taskName = taskName;
        this.dateTask = dateTask;
        this.timeTask = timeTask;
    }

    public Long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(Long disciplineId) {
        this.disciplineId = disciplineId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDateTask() {
        return dateTask;
    }

    public void setDateTask(String dateTask) {
        this.dateTask = dateTask;
    }

    public String getTimeTask() {
        return timeTask;
    }

    public void setTimeTask(String timeTask) {
        this.timeTask = timeTask;
    }
}
