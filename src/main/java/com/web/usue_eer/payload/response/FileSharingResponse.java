package com.web.usue_eer.payload.response;

public class FileSharingResponse {
    private Long userId;
    private Long fileId;
    private String fileName;
    private String dateAdd;

    public FileSharingResponse(Long fileId, String fileName, String dateAdd) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.dateAdd = dateAdd;
    }

    public FileSharingResponse(Long userId, Long fileId, String fileName, String dateAdd) {
        this.userId = userId;
        this.fileId = fileId;
        this.fileName = fileName;
        this.dateAdd = dateAdd;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
