package com.web.usue_eer.payload.request;


import jakarta.validation.constraints.NotBlank;

public class DisciplineRequest {
    @NotBlank(message = "Название не должно быть пустым")
    private String name;

    @NotBlank(message = "Группа не должна быть пустой")
    private String group;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
