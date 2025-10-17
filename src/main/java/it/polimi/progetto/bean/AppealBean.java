package it.polimi.progetto.bean;

import java.time.LocalDateTime;
import java.util.Optional;

public class AppealBean {
    private String code;
    private LocalDateTime dateTime;
    private String room;
    private VerbalBean verbal;


    public AppealBean(String code, LocalDateTime dateTime, String room, VerbalBean verbal) {
        this.code = code;
        this.dateTime = dateTime;
        this.room = room;
        this.verbal = verbal;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Optional<VerbalBean> getVerbal() {
        return Optional.ofNullable(verbal);
    }

    public void setVerbal(VerbalBean verbal) {
        this.verbal = verbal;
    }
}
