package util.processing.model;

import java.util.regex.Pattern;

public class TopicSelector {
    private static final Pattern INTEGER_PATTERN = Pattern.compile("-?\\d+");
    private static final Pattern FLOAT_PATTERN = Pattern.compile("-?\\d+(.\\d+)?([Ee][-+]?\\d+)?");
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
