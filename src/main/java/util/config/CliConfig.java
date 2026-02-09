package util.config;


import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Parameters;

import util.config.messages.ErrorMessages;
import util.config.validators.FileNameValidator;
import util.config.validators.PathValidator;
import util.config.validators.PrefixValidator;
import util.exceptions.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Command(
        name = "util",
        description = "Утилита для обработки файлов",
        version = "1.0.0",
        separator = " ",
        sortOptions = false
)
public class CliConfig{
    // Константы для паттернов извлечения
    private static final Pattern PARAM_PATTERN =
            Pattern.compile("parameter '([^']+)'");
    private static final Pattern OPTION_PATTERN =
            Pattern.compile("Unknown option: ([^;]+)");

    public static final CliConfig instance = new CliConfig();
    public static final CommandLine cmd = new CommandLine(instance);

    @Option(names = {"--help", "-h"},
            description = "Показать справку по использованию",
            usageHelp = true,
            order = 1
    )
    private boolean help;

    @Option(names = {"-p"},
            description = "Префикс имени выходных файлов, maxSize=50",
            paramLabel = "<префикс>",
            arity = "1",
            order = 2,
            converter = PrefixValidator.class
    )

    private String prefOut = "";

    @Option(names = {"-o"},
            description = "Путь для выходных файлов",
            paramLabel = "<путь>",
            arity = "1",
            order = 2,
            converter = PathValidator.class
    )
    private String pathOut = "";

    @Option(names = {"-a"},
            description = "Режим добавления в существующие файлы",
            order = 3
    )
    private boolean append;

    @Option(names = {"-s"},
            description = "Краткая статистика",
            paramLabel = "статистика",
            arity = "0",
            order = 4
    )
    private void setShortStats(boolean value) throws ParameterException {
        if (value) {
            if (statsLevel == StatsLevel.NONE)
                statsLevel = StatsLevel.SHORT;
            else {
                throw new ParameterException(cmd,
                        ErrorMessages.Conflicts.STATS_CONFLICT
                );
            }
        }
    }

    private StatsLevel statsLevel = StatsLevel.NONE;

    @Option(names = {"-f"},
            description = "Полная статистика",
            paramLabel = "статистика",
            arity = "0",
            order = 5
    )
    private void setFullStats(boolean value) {
        if (value) {
            if (statsLevel == StatsLevel.NONE)
                statsLevel = StatsLevel.FULL;
            else {
                throw new ParameterException(cmd,
                        ErrorMessages.Conflicts.STATS_CONFLICT
                );
            }
        }
    }

    @Parameters(
            description = "Входные файлы для обработки",
            paramLabel = "<файлы>",
            arity = "1..*", // Минимум один файл
            converter = FileNameValidator.class
    )
    private List<String> files;

    public static CliConfig getInstance() {return instance;}

    public String getPathOut() {
        return pathOut;
    }

    public String getPrefOut() {
        return prefOut;
    }

    public boolean appendMode() {
        return append;
    }

    public StatsLevel getStatsLevel() {
        return statsLevel;
    }

    public List<String> getFiles() {
        return files == null ? new ArrayList<>() : new ArrayList<>(this.files);
    }

    public void setConfig(String[] args) throws Exception {

        try {
            cmd.parseArgs(args);

            if (cmd.isUsageHelpRequested()) {
                cmd.usage(System.out);
                System.exit(0);
            }

        } catch (ParameterException e) {
            handleParameterError(e);
            System.exit(1);
        }
    }

    private void handleParameterError(ParameterException e) {
        String errorMessage = determineErrorMessage(e.getMessage());

        ExceptionHandler.printError(ErrorMessages.CLI_PREFIX + "\n" + errorMessage);
        ExceptionHandler.printInfo(ErrorMessages.USE_HELP);
    }
    private String determineErrorMessage(String originalMessage) {
        if (originalMessage.contains("Expected a value after parameter")) {
            return ErrorMessages.Parsing.missingValue(extractWithPattern(originalMessage, PARAM_PATTERN, "параметра"));
        } else if (originalMessage.contains("Unknown option")) {
            return ErrorMessages.Parsing.unknownOption(extractWithPattern(originalMessage, OPTION_PATTERN, "неизвестный флаг"));
        } else if (originalMessage.contains("Main parameters are required")) {
            return ErrorMessages.Parsing.NO_INPUT_FILES;
        } else {
            return originalMessage;
        }
    }

    private String extractWithPattern(String message, Pattern pattern, String defaultValue) {
        Matcher matcher = pattern.matcher(message);
        return matcher.find() ? matcher.group(1) : defaultValue;
    }
}
