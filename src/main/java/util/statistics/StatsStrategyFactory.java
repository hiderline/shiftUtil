package util.statistics;

import util.processing.model.Topic;

public class StatsStrategyFactory {
    private StatsStrategyFactory() {
        throw new AssertionError("Нельзя создать экземпляр фабрики");
    }
    public static StatsStrategy createForTopic(Topic topic) {
        switch (topic) {
            case INTEGER:
                return new IntegerStatsStrategy();
            case FLOAT:
                return new FloatStatsStrategy();
            case STRING:
                return new StringStatsStrategy();
            default:
                throw new IllegalArgumentException("Неизвестная тема: " + topic);
        }
    }
}
