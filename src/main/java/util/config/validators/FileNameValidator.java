package util.config.validators;

import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.TypeConversionException;
import util.config.messages.ErrorMessages;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileNameValidator implements ITypeConverter<String> {

    public String convert(String value) throws ParameterException {

        // Проверка запрещенных символов (Windows + Unix)
        if (value.matches(".*[<>:\"|?*\\x00-\\x1F].*")) {
            throw new TypeConversionException(
                    ErrorMessages.Validation.FORBIDDEN_CHARS_FILE
            );
        }

        Path filePath = Paths.get(value).toAbsolutePath().normalize();
        // Проверка существования файла
        if (!Files.exists(filePath)) {
            throw new TypeConversionException(
                    String.format(ErrorMessages.Validation.FILE_NOT_FOUND,
                            value, filePath.getParent().toString())
            );
        }
        // Дополнительная проверка: это файл, а не директория
        if (Files.isDirectory(filePath)) {
            throw new TypeConversionException(
                    String.format(ErrorMessages.Validation.PATH_IS_DIRECTORY, value)
            );
        }

        // Дополнительная проверка: файл доступен для чтения
        if (!Files.isReadable(filePath)) {
            throw new TypeConversionException(
                    String.format(ErrorMessages.Validation.FILE_NOT_READABLE, value)
            );
        }

        return value;
    }
}
