package com.stm.TicketService.util;

import com.stm.TicketService.DTO.FilterRequestDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class FilterValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return FilterRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        // any custom validation to be implemented here
        // for example we can move DB entity validation here
        // instead of in respective service

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
