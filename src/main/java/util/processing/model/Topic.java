package util.processing.model;

public enum Topic {
    INTEGER("integers"),
    FLOAT("floats"),
    STRING("strings");

    private final String description;

    Topic(String description) {
        this.description = description;
    }
    public String getDescription() {
        return this.description;
    }
}
