package com.web.usue_eer.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "discipline_files")
public class FileDiscipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Lob
    @Column(nullable = false)
    private byte[] fileData;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    private long fileSize;

    private String dateAdd;
    private String author;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private FolderDiscipline folder;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    public FileDiscipline() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public FolderDiscipline getFolder() {
        return folder;
    }

    public void setFolder(FolderDiscipline folder) {
        this.folder = folder;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
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
