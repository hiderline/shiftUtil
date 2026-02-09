package util.statistics;

import util.config.StatsLevel;
import util.processing.model.Topic;

public interface StatsStrategy {
    void update(Topic topic, String value);
    String getFormattedStats(StatsLevel statsLevel);
    long getCount();
    String getTopicName();
}
