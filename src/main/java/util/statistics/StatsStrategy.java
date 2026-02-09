package util.statistics;

import util.config.StatsLevel;
import util.processing.model.Topic;

public interface StatsStrategy {
    void update(String value);
    String getFormattedStats(StatsLevel statsLevel);
    long getCount();
    boolean hasData();
    String getTopicName();
}
