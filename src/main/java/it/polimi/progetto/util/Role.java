package it.polimi.progetto.util;

public enum Role {
    STUDENT("studente"),
    PROFESSOR("docente");
    private String description;
    Role(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public static Role fromDescription(String description) {
        for (Role role : Role.values()) {
            if (role.getDescription().equalsIgnoreCase(description)) {
                return role;
            }
        }
        throw new IllegalArgumentException("no role associated to " + description);
    }
}
