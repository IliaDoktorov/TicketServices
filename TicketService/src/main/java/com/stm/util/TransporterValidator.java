package com.stm.util;

import com.stm.models.Transporter;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class TransporterValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Transporter.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        if (errors.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> allErrors = errors.getFieldErrors();
            allErrors.forEach(error -> errorMsg
                    .append(error.getDefaultMessage())
                    .append(";")
            );
            throw new RequestException(errorMsg.toString());
        }
    }
}
