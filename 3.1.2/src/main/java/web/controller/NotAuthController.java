package web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.model.Role;
import web.model.User;
import web.security.UserValidation;
import web.service.RoleService;
import web.service.UserService;

import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class NotAuthController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserValidation userValidation;

    @GetMapping("/login")
    public String login() {
        System.out.println();
        return "nonAuthentication/login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userNew", new User());
        return "nonAuthentication/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userNew") @Validated User user, BindingResult bindingResult) {
        String roleDefault = "USER";
        Optional<Role> role = roleService.getRoleByName(roleDefault);
        userValidation.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "nonAuthentication/registration";
        }

        userService.save(user,
                Collections.singletonList(
                role.orElse(null)));

        return "redirect:/login";
    }
}
