package fr.bastiansmn.laponiegouv.exception;

import lombok.Getter;

@Getter
public enum FunctionalRule {

    FAMILY_0001("FAMILY_0001", "La famille n'existe pas"),
    FAMILY_0002("FAMILY_0002", "L'utilisateur n'existe pas dans la famille."),
    USER_0001("USER_0001", "L'utilisateur n'existe pas")
    ;

    private final String name;
    private final String message;
    FunctionalRule(final String name, final String message) {
        this.name = name;
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", this.getName(), this.getMessage());
    }

    public String toString(final Object... params) {
        return String.format("%s - %s", this.getName(), String.format(this.getMessage(), params));
    }

}
