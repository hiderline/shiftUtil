package util.config.validators;

import picocli.CommandLine.TypeConversionException;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.ParameterException;

public class PrefixValidator implements ITypeConverter<String> {

    public String convert(String value) throws ParameterException {

        if (!value.matches("^[a-zA-Z0-9_-]*$"))
            throw new TypeConversionException(
                    String.format("Префикс может содержать только буквы, цифры, _ и -%n" +
                            "Вы указали: %s", value)
            );

        if (value.length() > 50)
            throw new TypeConversionException(
                    String.format("Префикс слишком длинный (максимум 50 символов).%n" +
                            "Вы указали: %s", value)
            );

        return value;
    }
}
