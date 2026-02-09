package util.statistics;

import util.config.StatsLevel;

import java.util.concurrent.atomic.AtomicInteger;

public class StringStatsStrategy extends BaseStatsStrategy{
    private final AtomicInteger minLength = new AtomicInteger(Integer.MAX_VALUE);
    private final AtomicInteger maxLength = new AtomicInteger(0);

    public StringStatsStrategy(String topicName) {
        super(topicName);
    }

    @Override
    public void update(String value) {
        int length = value.length();
        count.incrementAndGet();

        // Обновляем минимальную длину и самую короткую строку
        minLength.updateAndGet(current -> {
            return Math.min(length, current);
        });

        // Обновляем максимальную длину и самую длинную строку
        maxLength.updateAndGet(current -> {
            return Math.max(length, current);
        });
    }

    @Override
    public String getFormattedStats(StatsLevel statsLevel) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatHeader());

        if (hasData()) {
            if (statsLevel == util.config.StatsLevel.FULL) {
                sb.append(String.format("Минимальная длина: %d\n", getMinLength()));
                sb.append(String.format("Максимальная длина: %d\n", getMaxLength()));

            }
        }

        return sb.toString();
    }

    private int getMinLength() {
        return count.get() > 0 ? minLength.get() : 0;
    }

    private int getMaxLength() {
        return count.get() > 0 ? maxLength.get() : 0;
    }
}
