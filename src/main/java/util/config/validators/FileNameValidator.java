package util.config.validators;

import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.TypeConversionException;

public class FileNameValidator implements ITypeConverter<String> {

    public String convert(String value) throws ParameterException {

        // Проверка запрещенных символов (Windows + Unix)
        if (value.matches(".*[<>:\"/\\\\|?*\\x00-\\x1F].*")) {
            throw new TypeConversionException(
                    "Имя файла содержит запрещенные символы: < > : \" / \\ | ? * или управляющие символы"
            );
        }

        // Проверка на допустимые имена файлов (не содержат путь)
        if (value.contains("\\") || value.contains("/")) {
            throw new TypeConversionException(
                    "Имя файла не должно содержать путь (символы / или \\)"
            );
        }

        return value;
    }
}
