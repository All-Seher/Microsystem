package web.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.annotation.IsPresent;
import web.dto.UserCreationDto;
import web.dto.UserDto;
import web.mapper.UserMapper;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;
import web.validation.Validation;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    UserService userService;
    RoleService roleService;
    UserMapper userMapper;
    Validation validation;

    @GetMapping("/users")
    @ResponseBody
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(
                userMapper.userListToUserDtoList(userService.findAll()));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@IsPresent @PathVariable("id") Long id) {

        return ResponseEntity.ok(
                userMapper.userToUserDTO(
                        userService.getById(id)));
    }

    @PostMapping("/user")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreationDto userCreationDto) {
        User user = userMapper.userCreationDtoToUser(userCreationDto, roleService);
        userService.save(user);

        return new ResponseEntity<>(userMapper.userToUserDTO(user), HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserCreationDto userCreationDto, @IsPresent @PathVariable("id") Long id) {

        User user = userMapper.userCreationDtoToUser(userCreationDto, roleService);

        validation.editValid(user, id); //проверка, чтобы новое имя не существовало в бд

        user.setId(id);
        userService.save(user);

        return ResponseEntity.ok(userMapper.userToUserDTO(user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@IsPresent @PathVariable("id") Long id) {
        validation.deleteValid(id);

        userService.deleteById(id);

        return ResponseEntity.ok("Пользователь удалён");
    }
}