package util.statistics;

import util.config.StatsLevel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicReference;

public class FloatStatsStrategy extends BaseStatsStrategy{
    private final AtomicReference<BigDecimal> min = new AtomicReference<>(null);
    private final AtomicReference<BigDecimal> max = new AtomicReference<>(null);
    private final AtomicReference<BigDecimal> sum = new AtomicReference<>(BigDecimal.ZERO);

    public FloatStatsStrategy(String topicName) {
        super(topicName);
    }

    @Override
    public void update(String value) {
        BigDecimal number = safeParseBigDecimal(value);
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
            BigDecimal currentMin = min.get();
            BigDecimal currentMax = max.get();
            BigDecimal currentSum = sum.get();

            sb.append(String.format("Минимальное значение: %s\n",
                    formatBigDecimal(currentMin)));
            sb.append(String.format("Максимальное значение: %s\n",
                    formatBigDecimal(currentMax)));
            sb.append(String.format("Сумма: %s\n",
                    formatBigDecimal(currentSum)));

            // Среднее значение
            if (currentSum != null && count.get() > 0) {
                BigDecimal average = currentSum
                        .divide(BigDecimal.valueOf(count.get()), 4, RoundingMode.HALF_UP);
                sb.append(String.format("Среднее значение: %s\n",
                        average.stripTrailingZeros().toPlainString()));
            }
        }

        return sb.toString();
    }

    private String formatBigDecimal(BigDecimal value) {
        if (value == null) return "N/A";
        // Убираем лишние нули для отображения
        return value.stripTrailingZeros().toPlainString();
    }
}
