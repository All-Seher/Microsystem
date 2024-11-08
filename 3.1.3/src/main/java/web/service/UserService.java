package web.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.exception.NotFoundException;
import web.model.Role;
import web.model.User;
import web.repository.UserRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    @Transactional
    public void save(User user) {
        user.setPassword(
                encryptPassword(user.getPassword()));

        userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    private String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
}