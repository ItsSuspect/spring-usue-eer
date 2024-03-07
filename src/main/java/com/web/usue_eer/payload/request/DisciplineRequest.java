package com.web.usue_eer.payload.request;


import com.web.usue_eer.entities.User;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;

public class DisciplineRequest {
    private String name;

    private Map<String, String> users;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getUsers() {
        return users;
    }

    public void setUsers(Map<String, String> users) {
        this.users = users;
    }
}
