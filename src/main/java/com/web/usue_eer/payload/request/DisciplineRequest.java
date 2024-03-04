package com.web.usue_eer.payload.request;


import com.web.usue_eer.entities.User;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class DisciplineRequest {
    private String name;

    private List<String> users;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
