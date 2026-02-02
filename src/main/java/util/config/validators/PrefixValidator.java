package util.config.validators;

import picocli.CommandLine.TypeConversionException;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.ParameterException;
import util.config.messages.ErrorMessages;

public class PrefixValidator implements ITypeConverter<String> {

    public String convert(String value) throws ParameterException {

        if (!value.matches("^[a-zA-Z0-9_-]*$"))
            throw new TypeConversionException(
                    ErrorMessages.Validation.invalidPrefixChars(value)
            );

        if (value.length() > 50)
            throw new TypeConversionException(
                    ErrorMessages.Validation.prefixTooLong(value)
            );

        return value;
    }
}
