package com.web.usue_eer.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "discipline_informations")
public class Information {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String informationOfDiscipline;
    private String informationOfTeacher;
    private String contacts;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    public Information() {
    }

    public Information(String informationOfDiscipline, String informationOfTeacher, String contacts, Discipline discipline) {
        this.informationOfDiscipline = informationOfDiscipline;
        this.informationOfTeacher = informationOfTeacher;
        this.contacts = contacts;
        this.discipline = discipline;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInformationOfDiscipline() {
        return informationOfDiscipline;
    }

    public void setInformationOfDiscipline(String informationOfDiscipline) {
        this.informationOfDiscipline = informationOfDiscipline;
    }

    public String getInformationOfTeacher() {
        return informationOfTeacher;
    }

    public void setInformationOfTeacher(String informationOfTeacher) {
        this.informationOfTeacher = informationOfTeacher;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }
}
