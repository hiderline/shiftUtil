package util.statistics;

import util.config.StatsLevel;

public interface StatsStrategy {
    void update(String value);
    String getFormattedStats(StatsLevel statsLevel);
    long getCount();
    boolean hasData();
}
