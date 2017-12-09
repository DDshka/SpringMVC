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
public class LoginValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(@Nullable Object target, Errors errors) {
        User user = (User) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "notEmpty");
        User user1 = userService.get(user.getName());
        if (!userService.exists(user.getName())) {
            errors.rejectValue("name", "user.noSuchUser");
            return;
        }

        User dbUser = userService.get(user.getName());
        if (!user.getPassword().equals(dbUser.getPassword())) {
            errors.rejectValue("password", "user.wrongPW");
        }
    }
}
