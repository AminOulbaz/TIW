package it.polimi.progetto.bean;

import it.polimi.progetto.util.EvaluationStatus;
import it.polimi.progetto.util.Vote;

public class SubscriberBean {
    private StudentBean student;
    private AppealBean appeal;
    private Vote vote;
    private EvaluationStatus status;

    public SubscriberBean(StudentBean student, AppealBean appeal, Vote vote, EvaluationStatus status) {
        this.student = student;
        this.appeal = appeal;
        this.vote = vote;
        this.status = status;
    }

    public StudentBean getStudent() {
        return student;
    }

    public void setStudent(StudentBean student) {
        this.student = student;
    }

    public AppealBean getAppeal() {
        return appeal;
    }

    public void setAppeal(AppealBean appeal) {
        this.appeal = appeal;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public EvaluationStatus getStatus() {
        return status;
    }

    public void setStatus(EvaluationStatus status) {
        this.status = status;
    }
}
