package com.web.usue_eer.payload.request;

import jakarta.validation.constraints.NotBlank;

public class GroupRequest {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
