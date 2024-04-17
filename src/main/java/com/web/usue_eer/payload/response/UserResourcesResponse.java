package com.web.usue_eer.payload.response;

public class UserResourcesResponse {
    private Long folderId;
    private Long fileId;
    private String fileName;
    private String dateAdd;

    public UserResourcesResponse(Long folderId, Long fileId, String fileName, String dateAdd) {
        this.folderId = folderId;
        this.fileId = fileId;
        this.fileName = fileName;
        this.dateAdd = dateAdd;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
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
}
