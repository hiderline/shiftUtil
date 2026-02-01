package util.config;

import com.beust.jcommander.*;
import util.config.validators.FileNameValidator;
import util.config.validators.PathValidator;
import util.config.validators.PrefixValidator;
import util.exceptions.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/*@Parameters(
        commandDescription = "Утилита для обработки текстовых файлов",
        commandDescriptionKey = "main.description"
)*/
public class CliConfig {
    public static final CliConfig instance = new CliConfig();

    private JCommander jCommander;

    @Parameter(names = {"--help", "-h"},
            description = "Показать справку по использованию",
            order = 1,
            help = true)
    private boolean help;

    @Parameter(names = {"-p"},
            description = "Префикс имени выходных файлов",
            order = 2,
            validateWith = PrefixValidator.class)
    private String prefOut;

    @Parameter(names = {"-o"},
            description = "Путь для выходных файлов",
            order = 3,
            validateWith = PathValidator.class)
    private String pathOut;

    @Parameter(names = {"-a"},
            order = 4,
            help = true,
            description = "Режим добавления в существующие файлы")
    private boolean append;

    @Parameter(names = {"-s"},
            order = 5,
            help = true,
            description = "Краткая статистика")
    private void setShortStats(boolean value) {
        if (value){
            if (statsLevel == StatsLevel.NONE)
                statsLevel = StatsLevel.SHORT;
            else throw new ParameterException(
                    "Нельзя использовать одновременно флаги -s (краткая статистика) и -f (полная статистика).\n" +
                    "Выберите только один тип статистики или не указывайте ни одного."
            );
        }
    }

    @Parameter(names = {"-f"},
            order = 6,
            help = true,
            description = "Полная статистика")
    private void setFullStats(boolean value) {
        if (value){
            if (statsLevel == StatsLevel.NONE)
                statsLevel = StatsLevel.FULL;
            else throw new ParameterException(
                    "Нельзя использовать одновременно флаги -s (краткая статистика) и -f (полная статистика).\n" +
                    "Выберите только один тип статистики или не указывайте ни одного."
            );
        }
    }

    private StatsLevel statsLevel = StatsLevel.NONE;

    @Parameter(description = "Входные файлы для обработки",
            required = true,
            validateWith = FileNameValidator.class)
    private List<String> files;

    public CliConfig() {
    }

    public static CliConfig getInstance() {return instance;}

    public boolean isHelp() {
        return help;
    }

    public String getPathOut() {
        return pathOut;
    }

    public String getPrefOut() {
        return prefOut;
    }

    public boolean appendMode() {
        return append;
    }


    public boolean isShortStats() {
        return statsLevel == StatsLevel.SHORT;
    }
    public boolean isFullStats() {
        return statsLevel == StatsLevel.FULL;
    }
    public StatsLevel getStatsLevel() {
        return statsLevel;
    }

    public List<String> getFiles() {
        return files == null ? new ArrayList<>() : new ArrayList<>(this.files);
    }

    public void printUsage() {
        if (jCommander != null) {
            jCommander.usage();
        }
    }

    public void setConfig(String[] args) {
        try {
            jCommander = JCommander.newBuilder()
                    .addObject(instance)
                    .programName("java -jar util.jar")
                    .build();

            jCommander.parse(args);

            // Если запрошена справка - показываем и выходим
            if (help) {
                printUsage();
                System.exit(0);
            }

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

    // Вложенный класс для валидации списка файлов
    public static class FileListValidator implements com.beust.jcommander.IParameterValidator {
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
    }

    // Кастомное исключение для ошибок валидации
    private static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }
}
