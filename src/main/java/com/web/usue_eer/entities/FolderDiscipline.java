package com.web.usue_eer.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "discipline_folders")
public class FolderDiscipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String folderName;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @ManyToOne
    @JoinColumn(name = "parent_folder_id")
    private FolderDiscipline parentFolder;

    private String dateAdd;

    public FolderDiscipline() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public FolderDiscipline getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(FolderDiscipline parentFolder) {
        this.parentFolder = parentFolder;
    }

    public String getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(String dateAdd) {
        this.dateAdd = dateAdd;
    }
}
