package web.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.security.UserValidation;
import web.service.RoleService;
import web.service.UserService;

import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final RoleService roleService;
    UserService userService;
    UserValidation userValidation;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/findAll";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        userService.findById(id).ifPresent(user -> model.addAttribute("userEdit", user));
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin/edit";

    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("userEdit") @Validated User user,
                       Model model,
                       BindingResult bindingResult) {

        userValidation.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            return "admin/edit";
        }

        userService.save(user);

        return "redirect:/admin";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("userAdd", new User());
        model.addAttribute("roles", roleService.getAllRoles());

        return "admin/add";
    }

    @PostMapping("/add")
    public String actAdding(@ModelAttribute("userAdd") @Validated User user,
                            Model model,
                            BindingResult bindingResult) {

        userValidation.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            return "admin/add";
        }

        userService.save(user);

        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute("userDelete") @Validated User user, BindingResult bindingResult) {

        userService.findById(user.getId()).ifPresent(user1 ->
                userValidation.validate(user1, bindingResult));

        if (!bindingResult.hasErrors()) {
            userService.deleteById(user.getId());
        }

        return "redirect:/admin";
    }
}
