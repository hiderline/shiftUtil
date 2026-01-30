package util.config.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class FileNameValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        Validators.validateFileName(name, value);
    }
}
