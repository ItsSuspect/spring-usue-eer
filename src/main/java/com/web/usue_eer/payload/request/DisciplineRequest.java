package com.web.usue_eer.payload.request;


import jakarta.validation.constraints.NotBlank;

public class DisciplineRequest {
    @NotBlank
    private String name;

    @NotBlank
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
