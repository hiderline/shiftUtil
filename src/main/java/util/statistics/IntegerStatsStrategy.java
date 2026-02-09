package util.statistics;

import util.config.StatsLevel;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

public class IntegerStatsStrategy extends BaseStatsStrategy{
    private final AtomicReference<BigInteger> min = new AtomicReference<>(null);
    private final AtomicReference<BigInteger> max = new AtomicReference<>(null);
    private final AtomicReference<BigInteger> sum = new AtomicReference<>(BigInteger.ZERO);


    public IntegerStatsStrategy(String topicName) {
        super(topicName);
    }

    @Override
    public void update(String value) {
        BigInteger number = safeParseBigInteger(value);
        count.incrementAndGet();

        // Обновляем сумму
        sum.updateAndGet(current -> current.add(number));

        // Обновляем минимум
        min.updateAndGet(current -> {
            if (current == null || number.compareTo(current) < 0) {
                return number;
            }
            return current;
        });

        // Обновляем максимум
        max.updateAndGet(current -> {
            if (current == null || number.compareTo(current) > 0) {
                return number;
            }
            return current;
        });
    }

    @Override
    public String getFormattedStats(StatsLevel statsLevel) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatHeader());

        if (statsLevel == util.config.StatsLevel.FULL && hasData()) {
            BigInteger currentMin = min.get();
            BigInteger currentMax = max.get();
            BigInteger currentSum = sum.get();

            sb.append(String.format("Минимальное значение: %s\n",
                    currentMin != null ? currentMin.toString() : "N/A"));
            sb.append(String.format("Максимальное значение: %s\n",
                    currentMax != null ? currentMax.toString() : "N/A"));
            sb.append(String.format("Сумма: %s\n",
                    currentSum != null ? currentSum.toString() : "N/A"));

            // Среднее значение
            if (currentSum != null && count.get() > 0) {
                BigDecimal average = new BigDecimal(currentSum)
                        .divide(BigDecimal.valueOf(count.get()), 2, java.math.RoundingMode.HALF_UP);
                sb.append(String.format("Среднее значение: %s\n", average.toPlainString()));
            }
        }

        return sb.toString();
    }
}
