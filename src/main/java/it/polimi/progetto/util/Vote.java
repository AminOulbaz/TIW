package it.polimi.progetto.util;

public enum Vote {
    EMPTY("<vuoto>"),
    ABSENT("assente"),
    RESCHEDULED("rimandato"),
    FAILED("riprovato"),
    EIGHTEEN("18"),
    NINETEEN("19"),
    TWENTY("20"),
    TWENTY_ONE("21"),
    TWENTY_TWO("22"),
    TWENTY_THREE("23"),
    TWENTY_FOUR("24"),
    TWENTY_FIVE("25"),
    TWENTY_SIX("26"),
    TWENTY_SEVEN("27"),
    TWENTY_EIGHT("28"),
    TWENTY_NINE("29"),
    THIRTY("30"),
    THIRTY_WITH_HONORS("30 e lode");

    private final String label;
    Vote(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
    public static Vote fromLabel(String label) {
        for (Vote status : Vote.values()) {
            if (status.getLabel().equalsIgnoreCase(label)) {
                return status;
            }
        }
        throw new IllegalArgumentException("no vote associated to " + label);
    }
}
