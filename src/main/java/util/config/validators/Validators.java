package util.config.validators;

import com.beust.jcommander.ParameterException;

public class Validators {
    private Validators() {
        throw new AssertionError("Нельзя создать экземпляр Validators");
    }
    public static void validateStatistic(String name, String value) throws ParameterException {

    }

    /**
     * Валидация префикса выходных файлов
     * @param name имя параметра (например, "-p")
     * @param value значение параметра
     * @throws ParameterException если валидация не пройдена
     */
    public static void validatePrefix(String name, String value) throws ParameterException {
        // Проверка на null или пустоту
        if (value == null || value.trim().isEmpty()) {
            throw new ParameterException(
                    String.format("Параметр %s требует указания префикса.%n" +
                            "Пример: %s myprefix_", name, name)
            );
        }

        // Проверка, что значение не начинается с дефиса (это не флаг)
        if (value.startsWith("-")) {
            throw new ParameterException(
                    String.format("Значение префикса не может начинаться с '-'.%n" +
                                    "Вы указали: %s%n" +
                                    "Пример правильного использования: %s myprefix_",
                            value, name)
            );
        }

        // Дополнительные проверки специфичные для префикса
        if (value.contains("/") || value.contains("\\")) {
            throw new ParameterException(
                    String.format("Префикс не может содержать символы пути (/ или \\).%n" +
                            "Вы указали: %s", value)
            );
        }

        // Проверка длины (опционально)
        if (value.length() > 50) {
            throw new ParameterException(
                    String.format("Префикс слишком длинный (максимум 50 символов).%n" +
                            "Вы указали: %s", value)
            );
        }
    }

    /**
     * Валидация пути к директории
     * @param name имя параметра (например, "-o")
     * @param value значение параметра
     * @throws ParameterException если валидация не пройдена
     */
    public static void validatePath(String name, String value) throws ParameterException {
        // Проверка на null или пустоту
        if (value == null || value.trim().isEmpty()) {
            throw new ParameterException(
                    String.format("Флаг %s требует указания пути.%n" +
                            "Пример: %s /path/to/output", name, name)
            );
        }

        // Проверка, что значение не начинается с дефиса
        if (value.startsWith("-")) {
            throw new ParameterException(
                    String.format("Путь не может начинаться с '-'.%n" +
                                    "Вы указали: %s%n" +
                                    "Пример правильного использования: %s /some/path",
                            value, name)
            );
        }

        // Проверка на абсолютный/относительный путь (опционально)
        if (!value.startsWith("/") && !value.startsWith("./") && !value.matches("[A-Za-z]:.*")) {
            // Можно предупредить или потребовать абсолютный путь
            // throw new ParameterException("Путь должен быть абсолютным");
        }

        // Проверка на запрещенные символы в пути
        if (value.contains("*") || value.contains("?") || value.contains("\"")) {
            throw new ParameterException(
                    String.format("Путь содержит запрещенные символы (*, ?, \").%n" +
                            "Вы указали: %s", value)
            );
        }
    }

    /**
     * Валидация имени файла
     * @param name имя параметра (например, "input_files")
     * @param value значение параметра (имя файла)
     * @throws ParameterException если валидация не пройдена
     */
    public static void validateFileName(String name, String value) throws ParameterException {

        if (value.startsWith("-")) {
            throw new ParameterException(
                    String.format("Входные файлы должны указываться после всех флагов.%n" +
                            "Обнаружен флаг вместо имени файла: %s", value)
            );
        }

        // Проверка на наличие пути
        if (value.contains("/") || value.contains("\\")) {
             throw new ParameterException(
                 String.format("Укажите только имя файла без пути: %s", value)
             );
        }

        // Проверка расширения (опционально)
        if (!value.contains(".")) {
            throw new ParameterException(
                    String.format("Имя файла должно содержать расширение: %s", value)
            );
        }

        // Проверка на безопасные символы
        String forbiddenChars = "<>:\"|?*";
        for (char c : forbiddenChars.toCharArray()) {
            if (value.contains(String.valueOf(c))) {
                throw new ParameterException(
                        String.format("Имя файла содержит запрещенный символ '%c': %s", c, value)
                );
            }
        }
    }
}
