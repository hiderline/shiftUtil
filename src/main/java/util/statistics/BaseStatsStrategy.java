package util.statistics;

import java.util.concurrent.atomic.AtomicLong;

public abstract class BaseStatsStrategy implements StatsStrategy{
    protected final String topicName;
    protected final AtomicLong count = new AtomicLong(0);
    protected final AtomicLong totalLength = new AtomicLong(0);

    public BaseStatsStrategy(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public long getCount() {
        return count.get();
    }

    @Override
    public String getTopicName() {
        return topicName;
    }

    protected String formatHeader() {
        return "=== Статистика для " + topicName + " ===\n" +
                "Количество элементов: " + getCount() + "\n";
    }
}
