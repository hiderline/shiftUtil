package util.config.converters;

import picocli.CommandLine;
import util.config.StatsLevel;

// Конвертер, который получает ИМЯ ФЛАГА!
public class StatsFlagConverter implements CommandLine.ITypeConverter<StatsLevel> {
    @Override
    public StatsLevel convert(String flagName) throws Exception {
        // flagName будет "-s" или "-f"!
        switch (flagName) {
            case "-s": return StatsLevel.SHORT;
            case "-f": return StatsLevel.FULL;
            default:
                // Если передали значение напрямую
                if ("short".equalsIgnoreCase(flagName)) return StatsLevel.SHORT;
                if ("full".equalsIgnoreCase(flagName)) return StatsLevel.FULL;
                throw new CommandLine.TypeConversionException(
                        "Неизвестный флаг или значение: " + flagName);
        }
    }
}
