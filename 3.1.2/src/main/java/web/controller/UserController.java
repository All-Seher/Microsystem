package web.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.model.User;

@AllArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping()
    public String getUser(@AuthenticationPrincipal User user, Model model) {
       model.addAttribute("user", user);

        return "user/findByName";
    }
}
