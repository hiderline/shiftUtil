package util.processing.model;

import java.util.regex.Pattern;

public class TopicSelector {
    // Целые числа:
    // -?          : необязательный знак минус
    // (0|[1-9]\d*): либо 0, либо цифра 1-9, за которой следует 0 или более цифр
    // Это исключает ведущие нули
    private static final Pattern INTEGER_PATTERN = Pattern.compile("-?(0|[1-9]\\d*)");

    // Вещественные числа:
    // -?                : необязательный знак минус
    // (0|[1-9]\d*)      : целая часть (без ведущих нулей)
    // (\\.\d+)?         : необязательная дробная часть
    // ([Ee][-+]?\d+)?   : необязательная экспонента
    private static final Pattern FLOAT_PATTERN = Pattern.compile("-?(0|[1-9]\\d*)(\\.\\d+)?([Ee][-+]?\\d+)?");

    private static final TopicSelector instance = new TopicSelector();

    public Topic determineTopic(String value) {
        if (INTEGER_PATTERN.matcher(value).matches())
            return Topic.INTEGER;
        else if (FLOAT_PATTERN.matcher(value).matches()) {
            return Topic.FLOAT;
        } else
            return Topic.STRING;
    }

    public static TopicSelector getInstance() { return instance; }
}
