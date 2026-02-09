package util.statistics;

import util.processing.model.Topic;

public class StatsStrategyFactory {
    public static StatsStrategy createForTopic(Topic topic) {
        switch (topic) {
            case INTEGER:
                return new IntegerStatsStrategy(topic.getDescription());
            case FLOAT:
                return new FloatStatsStrategy(topic.getDescription());
            case STRING:
                return new StringStatsStrategy(topic.getDescription());
            default:
                throw new IllegalArgumentException("Неизвестная тема: " + topic);
        }
    }
}
