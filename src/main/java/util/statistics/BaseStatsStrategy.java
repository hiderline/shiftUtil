package util.statistics;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

public abstract class BaseStatsStrategy implements StatsStrategy{
    protected final String topicName;
    protected final AtomicLong count = new AtomicLong(0);

    BaseStatsStrategy(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public long getCount() {
        return count.get();
    }

    @Override
    public boolean hasData() {
        return count.get() > 0;
    }

    @Override
    public String getTopicName() {
        return topicName;
    }

    protected String formatHeader() {
        return "=== Статистика для " + topicName + " ===\n" +
                "Количество элементов: " + getCount() + "\n";
    }

    protected BigDecimal safeParseBigDecimal(String value) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Неверный числовой формат: " + value, e);
        }
    }

    protected BigInteger safeParseBigInteger(String value) {
        try {
            return new BigInteger(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Неверный целочисленный формат: " + value, e);
        }
    }
}
