package util.config.validators;

import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.TypeConversionException;
import util.config.messages.ErrorMessages;

public class FileNameValidator implements ITypeConverter<String> {

    public String convert(String value) throws ParameterException {

        // Проверка запрещенных символов (Windows + Unix)
        if (value.matches(".*[<>:\"/\\\\|?*\\x00-\\x1F].*")) {
            throw new TypeConversionException(
                    ErrorMessages.Validation.FORBIDDEN_CHARS_FILE
            );
        }

        // Проверка на допустимые имена файлов (не содержат путь)
        if (value.contains("\\") || value.contains("/")) {
            throw new TypeConversionException(
                    ErrorMessages.Validation.NO_PATH_IN_FILENAME
            );
        }

        return value;
    }
}
