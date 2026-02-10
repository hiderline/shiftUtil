package util.config.messages;

public class ErrorMessages {
    // Общие сообщения
    public static final String CLI_PREFIX = "Ошибка в параметрах командной строки:";
    public static final String USE_HELP = "\nИспользуйте --help для получения полной справки:";

    // Сообщения об ошибках парсинга
    public static class Parsing {
        public static String missingValue(String param) {
            return String.format(
                    "После флага %s не указано значение.\n" +
                            "Пример: %s значение",
                    param, param
            );
        }

        public static String unknownOption(String option) {
            return String.format(
                    "Неизвестный флаг: %s\n" +
                            "Проверьте правильность написания флага.",
                    option
            );
        }

        public static final String NO_INPUT_FILES =
                "Не указаны входные файлы для обработки.\n" +
                        "Укажите файлы в конце команды.";
    }

    // Сообщения валидации
    public static class Validation {
        public static final String FORBIDDEN_CHARS_FILE =
                "Имя файла содержит запрещенные символы: < > : \" / \\ | ? * или управляющие символы";

        public static final String NO_PATH_IN_FILENAME =
                "Имя файла не должно содержать путь (символы / или \\)";

        public static final String FORBIDDEN_CHARS_PATH =
                "Путь содержит запрещенные символы: < > : \" | ? * или управляющие символы";

        public static final String INVALID_PATH_FORMAT = "Некорректный формат пути";

        public static String invalidPrefixChars(String value) {
            return String.format(
                    "Префикс может содержать только буквы, цифры, _ и -%n" +
                            "Вы указали: %s", value
            );
        }

        public static String prefixTooLong(String value) {
            return String.format(
                    "Префикс слишком длинный (максимум 50 символов).%n" +
                            "Вы указали: %s", value
            );
        }

        public static final String FILE_NOT_FOUND = "Файл '%s' не найден в директории main/resources";
        public static final String PATH_IS_DIRECTORY = "'%s' является директорией, а не файлом";
        public static final String FILE_NOT_READABLE = "Файл '%s' недоступен для чтения";

        public static String invalidPathSyntax(String details) {
            return "Некорректный синтаксис пути: " + details;
        }
    }

    // Сообщения о конфликтах
    public static class Conflicts {
        public static final String STATS_CONFLICT =
                "Нельзя использовать одновременно флаги -s (краткая статистика) и -f (полная статистика)\n" +
                        "Выберите только один тип статистики или не указывайте ни одного.";
    }
}
