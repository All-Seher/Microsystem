package web.security;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import web.model.User;

import java.util.Objects;

@AllArgsConstructor
@Component
public class UserValidation implements Validator {

    private CustomUserDetailsService customUserDetailsService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        User user = (User) target;
        User userFromRepo;
        try {
            userFromRepo = (User) customUserDetailsService.loadUserByUsername(user.getUsername());
            if (errors.getObjectName().equals("userNew") && Objects.equals(user.getUsername(), userFromRepo.getUsername())) {
                errors.rejectValue("username", "", "Данного пользователя нельзя зарегистрировать");
            } else if (errors.getObjectName().equals("userAdd") && Objects.equals(user.getUsername(), userFromRepo.getUsername())) {
                errors.rejectValue("username", "", "Данного пользователя нельзя добавить");
            } else if (errors.getObjectName().equals("userEdit") && Objects.equals(user.getId(), userFromRepo.getId())) {
                errors.rejectValue("username", "", "Данного пользователя нельзя Редактировать");
            } else if (errors.getObjectName().equals("userEdit") && Objects.equals(user.getUsername(), userFromRepo.getUsername())) {
                errors.rejectValue("username", "", "Пользователь с таким именем уже существует");
            } else if (errors.getObjectName().equals("userDelete")) {

                User authentication = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                if (Objects.equals(authentication.getId(), userFromRepo.getId())) {
                    errors.rejectValue("username", "", "");
                }

            }
        } catch (UsernameNotFoundException e) {

            try {
                User authentication = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (errors.getObjectName().equals("userEdit") && Objects.equals(user.getId(), authentication.getId())) {
                    errors.rejectValue("username", "", "Данного пользователя нельзя Редактировать");
                }
            } catch (ClassCastException cce) {
                return;
            }
        }
    }
}