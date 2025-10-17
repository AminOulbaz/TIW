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

    public static void main(String[] args) {
        Role role = Role.valueOf("STUDENT");
        System.out.println(role.getDescription());
    }
}
