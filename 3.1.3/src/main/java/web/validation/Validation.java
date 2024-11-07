package web.validation;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import web.model.User;
import web.repository.UserRepository;

@Component
@AllArgsConstructor
public class Validation {

    private UserRepository userRepository;

    public void editValid(User user, long id) throws ValidationException {
        User userFromRepo = userRepository.findByUsername(
                user.getUsername()).orElse(null);

        if (userFromRepo == null) {
            return;
        }

        if(userFromRepo.getId() != id || id == 1) {
            throw new ValidationException("Пользователь с данным именем уже существует");
        }
    }

    public void deleteValid(long id) throws ValidationException {
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authUser.getId() == id || id == 1) {
            throw new ValidationException("Данного пользователя нельзя удалить");
        }
    }
}
