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
    public static EvaluationStatus fromMeaning(String meaning) {
        for (EvaluationStatus status : EvaluationStatus.values()) {
            if (status.getMeaning().equalsIgnoreCase(meaning)) {
                return status;
            }
        }
        throw new IllegalArgumentException("no status associated to " + meaning);
    }
}
