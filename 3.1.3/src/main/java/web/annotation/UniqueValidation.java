package web.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import web.model.User;
import web.repository.UserRepository;

import java.util.Optional;

@AllArgsConstructor
public class UniqueValidation implements ConstraintValidator<Unique, String> {

    private UserRepository userRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Optional<User> user = userRepository.findByUsername(value);
        return value != null && user.isEmpty();
    }
}