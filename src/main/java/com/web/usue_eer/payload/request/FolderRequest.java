package com.web.usue_eer.payload.request;

public class FolderRequest {
    private String name;
    private Long parentFolder;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(Long parentFolder) {
        this.parentFolder = parentFolder;
    }
}
