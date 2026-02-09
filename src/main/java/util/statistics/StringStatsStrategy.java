package util.statistics;

import util.config.StatsLevel;
import util.processing.model.Topic;

import java.util.concurrent.atomic.AtomicInteger;

public class StringStatsStrategy extends BaseStatsStrategy{
    private final AtomicInteger minLength = new AtomicInteger(Integer.MAX_VALUE);
    private final AtomicInteger maxLength = new AtomicInteger(0);

    StringStatsStrategy() {
        super(Topic.STRING.getDescription());
    }

    @Override
    public void update(String value) {
        int length = value.length();
        count.incrementAndGet();

        // Обновляем минимальную длину
        minLength.updateAndGet(current -> {
            return Math.min(length, current);
        });

        // Обновляем максимальную длину
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
                sb.append(String.format("Минимальная длина строки: %d\n", getMinLength()));
                sb.append(String.format("Максимальная длина строки: %d\n", getMaxLength()));

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
