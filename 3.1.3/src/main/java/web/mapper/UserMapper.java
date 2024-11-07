package web.mapper;

import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import web.dto.UserCreationDto;
import web.dto.UserDto;
import web.model.Role;
import web.model.User;
import web.repository.RoleRepository;
import web.service.RoleService;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    User userCreationDtoToUser(UserCreationDto userCreationDto, @Context RoleService roleService);
    UserDto userToUserDTO(User user);
    List<UserDto> userListToUserDtoList(List<User> users);

    default List<String> mapRoles(List<Role> roles) {
        return roles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());
    }

    default List<Role> mapRolesToRole(List<String> roleNames, @Context RoleService roleService) {
        return roleNames.stream()
                .map(roleName -> roleService.getRoleByName(roleName).orElse(null))
                .collect(Collectors.toList());
    }
}