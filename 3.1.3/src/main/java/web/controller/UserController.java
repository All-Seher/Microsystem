package web.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.dto.UserDto;
import web.mapper.UserMapper;
import web.model.User;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    UserMapper userMapper;

    @GetMapping
    public ResponseEntity<UserDto> getUser(@AuthenticationPrincipal User user) {
       return ResponseEntity.ok(userMapper.userToUserDTO(user));
    }
}