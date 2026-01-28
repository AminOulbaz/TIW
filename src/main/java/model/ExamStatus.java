package model;

import java.util.Arrays;
import java.util.List;

public enum ExamStatus {
    NOTINSERTED("non inserito"),
    INSERTED("inserito"),
    PUBLISHED("pubblicato"),
    REFUSED("rifiutato"),
    VERBALIZED("verbalizzato");
    private String label;
    ExamStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static ExamStatus getExamStatus(String label) {
        return Arrays.stream(values())
                .filter(examStatus -> examStatus.getLabel().equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static List<ExamStatus> getExamsStatus() {
        return Arrays.asList(values());
    }
}
