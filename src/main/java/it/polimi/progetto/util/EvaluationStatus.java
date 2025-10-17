package it.polimi.progetto.util;

public enum EvaluationStatus {
    NOTINSERTED("non inserito"),
    INSERTED("inserito"),
    PUBLISHED("pubblicato"),
    REFUSED("rifiutato"),
    VERBALIZED("verbalizzato");
    private String meaning;
    EvaluationStatus(String meaning) {
        this.meaning = meaning;
    }

    public String getMeaning() {
        return meaning;
    }
}
