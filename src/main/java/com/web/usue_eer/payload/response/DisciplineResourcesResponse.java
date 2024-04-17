package com.web.usue_eer.payload.response;

public class DisciplineResourcesResponse {
    private Long fileId;
    private Long folderId;
    private String fileName;
    private String dateAdd;
    private String author;

    public DisciplineResourcesResponse(Long fileId, Long folderId, String fileName, String dateAdd, String author) {
        this.fileId = fileId;
        this.folderId = folderId;
        this.fileName = fileName;
        this.dateAdd = dateAdd;
        this.author = author;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(String dateAdd) {
        this.dateAdd = dateAdd;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
