package util.config;


import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Parameters;
import java.util.concurrent.Callable;

import util.config.converters.StatsFlagConverter;
import util.config.validators.FileNameValidator;
import util.config.validators.PathValidator;
import util.config.validators.PrefixValidator;
import util.exceptions.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Command(
        name = "util",
        description = "Утилита для обработки файлов",
        version = "1.0.0",
        separator = " ",
        sortOptions = false
)
public class CliConfig{
    public static final CliConfig instance = new CliConfig();
    public static final CommandLine cmd = new CommandLine(instance);

    @Option(names = {"--help", "-h"},
            description = "Показать справку по использованию",
            usageHelp = true,
            order = 1
    )
    private boolean help;

    @Option(names = {"-p"},
            description = "Префикс имени выходных файлов",
            defaultValue = "",
            paramLabel = "<префикс>",
            arity = "1",
            order = 2
            //validateWith = PrefixValidator.class
    )
    private String prefOut = "";

    @Option(names = {"-o"},
            description = "Путь для выходных файлов",
            defaultValue = "",
            paramLabel = "<путь>",
            order = 2
            //validateWith = PathValidator.class
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
                            "Нельзя использовать одновременно флаги -s (краткая статистика) и -f (полная статистика)\n" +
                            "Выберите только один тип статистики или не указывайте ни одного."
                );
            }
        }
    }

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
                        "Нельзя использовать одновременно флаги -s (краткая статистика) и -f (полная статистика)\n" +
                        "Выберите только один тип статистики или не указывайте ни одного."
                );
            }
        }
    }

    private StatsLevel statsLevel = StatsLevel.NONE;



    @Parameters(
            description = "Входные файлы для обработки",
            paramLabel = "<файлы>",
            arity = "1..*" // Минимум один файл
            //validateWith = FileNameValidator.class
    )
    private List<String> files;

    public CliConfig() {
    }

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
        CommandLine cmd = new CommandLine(instance);

        try {
            cmd.parseArgs(args);

            if (cmd.isUsageHelpRequested()) {
                cmd.usage(System.out);
                System.exit(0);
            }


            //validateParameters();



        } catch (ParameterException e) {
            handleParameterError(e);
            System.exit(1);
        } /*catch (ValidationException e) {
            ExceptionHandler.printError(e.getMessage());
            ExceptionHandler.printInfo("Используйте --help для получения справки");
            System.exit(1);
        }
*/
    }

    private void validateParameters() {

    }

    /*private void validateParameters() throws ValidationException {
        // Проверка взаимоисключающих флагов -s и -f
        if (shortStats && fullStats) {
            throw new ValidationException(
                    "Нельзя использовать одновременно флаги -s (краткая статистика) и -f (полная статистика).\n" +
                            "Выберите только один тип статистики или не указывайте ни одного."
            );
        }

        // Проверка наличия значений после -p и -o (если они указаны)
        if (prefOut != null && prefOut.trim().isEmpty()) {
            throw new ValidationException(
                    "Флаг -p требует указания префикса.\n" +
                            "Пример: -p myprefix_"
            );
        }

        if (pathOut != null && pathOut.trim().isEmpty()) {
            throw new ValidationException(
                    "Флаг -o требует указания пути.\n" +
                            "Пример: -o /path/to/output"
            );
        }

        // Проверка наличия входных файлов
        if (files == null || files.isEmpty()) {
            throw new ValidationException(
                    "Не указаны входные файлы для обработки.\n" +
                            "Укажите хотя бы один файл в конце команды.\n" +
                            "Пример: java -jar util.jar -f -o /путь -p префикс_ file1.txt file2.txt"
            );
        }

        // Проверка, что файлы действительно указаны в конце
        // (JCommander уже гарантирует это, но можно добавить дополнительную проверку)
        for (String file : files) {
            if (file.startsWith("-")) {
                throw new ValidationException(
                        "Входные файлы должны указываться в конце команды, после всех флагов.\n" +
                                "Обнаружен флаг среди файлов: " + file
                );
            }
        }
    }*/

    private void handleParameterError(ParameterException e) {
        String errorMessage;

        // Анализируем тип ошибки для более понятного сообщения
        if (e.getMessage().contains("Expected a value after parameter")) {
            String param = extractParamFromMessage(e.getMessage());
            errorMessage = String.format(
                    "После флага %s не указано значение.\n" +
                            "Пример: %s значение",
                    param, param
            );
        } else if (e.getMessage().contains("Unknown option")) {
            String option = extractOptionFromMessage(e.getMessage());
            errorMessage = String.format(
                    "Неизвестный флаг: %s\n" +
                            "Проверьте правильность написания флага.",
                    option
            );
        } else if (e.getMessage().contains("Main parameters are required")) {
            errorMessage = "Не указаны входные файлы для обработки.\n" +
                    "Укажите файлы в конце команды.";
        } else {
            errorMessage = e.getMessage();
        }

        ExceptionHandler.printError("Ошибка в параметрах командной строки: " + errorMessage);
        ExceptionHandler.printInfo("\nИспользуйте --help для получения полной справки:");
    }

    private String extractParamFromMessage(String message) {
        // Извлекаем название параметра из сообщения об ошибке
        try {
            String[] parts = message.split("'");
            if (parts.length > 1) {
                return parts[1];
            }
        } catch (Exception e) {
            // Если не удалось извлечь, возвращаем общее сообщение
        }
        return "параметра";
    }

    private String extractOptionFromMessage(String message) {
        // Извлекаем название опции из сообщения об ошибке
        try {
            String[] parts = message.split(":");
            if (parts.length > 1) {
                return parts[1].trim();
            }
        } catch (Exception e) {
            // Если не удалось извлечь, возвращаем общее сообщение
        }
        return "неизвестный флаг";
    }

    /*// Вложенный класс для валидации списка файлов
    public static class FileListValidator implements IParameterValidator {
        @Override
        public void validate(String name, String value) throws ParameterException {
            // JCommander вызывает этот метод для каждого файла отдельно,
            // поэтому здесь можно проверить отдельные файлы
            if (value == null || value.trim().isEmpty()) {
                throw new ParameterException("Имя файла не может быть пустым");
            }
            if (value.startsWith("-")) {
                throw new ParameterException(
                        "Входные файлы должны указываться после всех флагов. " +
                                "Обнаружен флаг вместо имени файла: " + value
                );
            }
        }
    }*/

    // Кастомное исключение для ошибок валидации
    private static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }
}
