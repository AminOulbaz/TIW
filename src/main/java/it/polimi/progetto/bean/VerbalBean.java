package it.polimi.progetto.bean;

import java.time.LocalDateTime;

public class VerbalBean {
    private String code;
    private LocalDateTime dateTimeOfVerbal;


    public VerbalBean(String code, LocalDateTime dateTimeOfVerbal) {
        this.code = code;
        this.dateTimeOfVerbal = dateTimeOfVerbal;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getDateTimeOfVerbal() {
        return dateTimeOfVerbal;
    }

    public void setDateTimeOfVerbal(LocalDateTime dateTimeOfVerbal) {
        this.dateTimeOfVerbal = dateTimeOfVerbal;
    }
}
