package com.web.usue_eer.payload.response;

public class FileSharingUserResponse {
    private Long id;
    private String user;

    public FileSharingUserResponse(Long id, String user) {
        this.id = id;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
