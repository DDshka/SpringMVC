package com.ddshka.service.validators;

import com.ddshka.model.User;
import com.ddshka.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class RegistrationValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(@Nullable Object target, Errors errors) {
        User user = (User) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "notEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "notEmpty");
        if (userService.exists(user.getName())) {
            errors.rejectValue("name", "user.alreadyExists");
        }
    }
}
