package com.web.usue_eer.payload.request;

public class InformationRequest {
    private Long id;
    private String informationOfDiscipline;
    private String informationOfTeacher;
    private String contacts;

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
}
