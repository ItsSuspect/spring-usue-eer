package com.web.usue_eer.payload.response;

public class AnnouncementResponse {
    private Long id;
    private String name;
    private String content;
    private String date;
    private String owner;

    public AnnouncementResponse(Long id, String name, String content, String date, String owner) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.date = date;
        this.owner = owner;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
