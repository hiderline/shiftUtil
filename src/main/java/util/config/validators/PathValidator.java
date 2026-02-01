package util.config.validators;

import picocli.CommandLine.TypeConversionException;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.ParameterException;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class PathValidator implements ITypeConverter<String> {

    public String convert(String value) throws ParameterException {

        // Проверка на запрещённые символы
        if (value.matches(".*[<>:\"|?*\\x00-\\x1F].*"))
            throw new TypeConversionException(
                    "Путь содержит запрещенные символы: < > : \\\" | ? * или управляющие символы"
            );

        // Проверка на допустимые относительные пути
        // Разрешаем: ../, ./, /, буква диска (C:), ~ (Unix home)
        if (!value.matches("^(\\.\\.?[/\\\\].*|[/\\\\].*|[A-Za-z]:[/\\\\].*|~[/\\\\].*|\\w+).*$")) {
            throw new TypeConversionException("Некорректный формат пути");
        }

        // Проверка на корректность синтаксиса пути (не вызывает InvalidPathException)
        try {
            Paths.get(value);
        } catch (InvalidPathException e) {
            throw new TypeConversionException("Некорректный синтаксис пути: " + e.getMessage());
        }

        return value;
    }
}
