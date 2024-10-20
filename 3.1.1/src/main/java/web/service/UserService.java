package web.service;

import web.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void save(User user);
    List<User> findAll();
    Optional<User> findById(long id);
    void delete(User user);
}
