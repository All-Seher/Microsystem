package web.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import web.model.Role;
import web.model.User;
import web.repository.RoleRepository;
import web.repository.UserRepository;

import java.util.List;

@Repository
@AllArgsConstructor
public class DataBaseInit {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initDB() {
        initRole();
        initUser();
    }

    private void initRole() {
        if (roleRepository.findByRoleName("ADMIN").isEmpty() &&
                roleRepository.findByRoleName("USER").isEmpty()) {
            Role adminRole = Role.builder()
                    .roleName("ADMIN")
                    .build();

            Role userRole = Role.builder()
                    .roleName("USER")
                    .build();

            roleRepository.save(adminRole);
            roleRepository.save(userRole);
        }
    }

    private void initUser() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            List<Role> roles = roleRepository.findAll();

            User user = User.builder()
                    .username("admin")
                    .surname("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles(roles)
                    .build();

            userRepository.save(user);
        }
    }
}
