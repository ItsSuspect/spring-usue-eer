package com.web.usue_eer.payload.response;

public class FilesResponse {
    private final String fileName;
    private final byte[] fileData;
    private final String fileType;
    private final long fileSize;

    public FilesResponse(String fileName, byte[] fileData, String fileType, long fileSize) {
        this.fileName = fileName;
        this.fileData = fileData;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public String getFileType() {
        return fileType;
    }

    public long getFileSize() {
        return fileSize;
    }
}
