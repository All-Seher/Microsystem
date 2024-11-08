package web.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import web.model.Role;
import web.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RoleService {

    RoleRepository repo;

    public Optional<Role> getRoleByName(String role) {
        return repo.findByRoleName(role);
    }

    public List<Role> getAllRoles() {
     return repo.findAll();
    }
}
