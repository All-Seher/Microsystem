package web.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import web.exception.NotFoundException;
import web.model.User;
import web.repository.UserRepository;

import java.util.Optional;

public class IsPresentValidation implements ConstraintValidator<IsPresent, Long> {

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new ValidationException("Пользователь не найден");
        }

        return true;
    }
}
