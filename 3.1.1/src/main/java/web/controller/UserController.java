package web.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.UserService;

import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/")
public class UserController {

    UserService userService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("users", userService.findAll());

        return "listUsers";
    }

    @GetMapping("/create-user")
    public String createUser(Model model) {
        model.addAttribute("user", new User());
        return "createAndSave";
    }

    @PostMapping("/")
    public String createUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        userService.findById(id).ifPresent(user -> model.addAttribute("user", user));

        return "createAndSave";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        Optional<User> user = userService.findById(id);
        user.ifPresent(u -> userService.delete(u));
        return "redirect:/";
    }
}
