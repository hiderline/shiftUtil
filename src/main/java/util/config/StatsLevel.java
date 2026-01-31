package util.config;

public enum StatsLevel {
    NONE("без статистики"),
    SHORT("краткая статистика"),
    FULL("полная статистика");

    private final String description;

    StatsLevel(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
