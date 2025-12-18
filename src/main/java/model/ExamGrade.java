package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum ExamGrade {
    EMPTY("<vuoto>"),
    ABSENT("assente"),
    DEFERRED("rimandato"),
    FAILED("riprovato"),
    GRADE_18("18"),
    GRADE_19("19"),
    GRADE_20("20"),
    GRADE_21("21"),
    GRADE_22("22"),
    GRADE_23("23"),
    GRADE_24("24"),
    GRADE_25("25"),
    GRADE_26("26"),
    GRADE_27("27"),
    GRADE_28("28"),
    GRADE_29("29"),
    GRADE_30("30"),
    GRADE_30_LODE("30 e lode");
    private String label;
    ExamGrade(String label) {
        this.label = label;
    }
    public static ExamGrade getExamGrade(String label) {
        return Arrays.stream(values())
                .filter(examGrade -> examGrade.getLabel().equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
    public static List<ExamGrade> getExamGrades() {
        return Arrays.asList(values());
    }
    public String getLabel() {
        return label;
    }
}

