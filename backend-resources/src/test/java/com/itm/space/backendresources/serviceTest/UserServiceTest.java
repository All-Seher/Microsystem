package com.itm.space.backendresources.serviceTest;

import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.api.response.UserResponse;
import com.itm.space.backendresources.exception.BackendResourcesException;
import com.itm.space.backendresources.mapper.UserMapper;
import com.itm.space.backendresources.service.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.MappingsRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceTest {

    private static final String USERNAME = "test-username";
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "test-password";
    private static final String FIRST_NAME = "test-firstName";
    private static final String LAST_NAME = "test-lastName";

    @Autowired
    private UserServiceImpl userService;
    @MockBean
    private Keycloak keycloakClient;
    @MockBean
    private UserMapper userMapper;
    private UsersResource mockedUsersResource;
    private UserRequest userRequest;
    private Response mockResponse;
    RealmResource mockedRealmResource;

    @BeforeEach
    void setUp() {

        userRequest = UserRequest.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();

        mockedRealmResource = mock(RealmResource.class);
        mockedUsersResource = mock(UsersResource.class);
        mockResponse = mock(Response.class);

        when(keycloakClient.realm(any())).thenReturn(mockedRealmResource);
        when(mockedRealmResource.users()).thenReturn(mockedUsersResource);
    }

    @Test
    public void createShouldCreateUser() {

        when(mockResponse.getStatusInfo()).thenReturn(Response.Status.CREATED);
        when(mockedUsersResource.create(any(UserRepresentation.class))).thenReturn(mockResponse);

        userService.createUser(userRequest);

        verify(mockedUsersResource, times(1)).create(any(UserRepresentation.class));
    }


    @Test
    public void shouldThrowExceptionOnCreateUser() {
        when(mockedUsersResource.create(any(UserRepresentation.class)))
                .thenThrow(new WebApplicationException(Response.status(Response.Status.CONFLICT).build()));

        BackendResourcesException exception = assertThrows(BackendResourcesException.class,
                () -> userService.createUser(userRequest));

        Assertions.assertEquals("HTTP 409 Conflict", exception.getMessage());
        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
    }

    @Test
    public void getUserByIdShouldReturnUserDetailsWithRolesAndGroups() {
        UUID userUUID = UUID.randomUUID();
        UserResource mockedUserResource = mock(UserResource.class);
        UserRepresentation mockedUserRepresentation = mock(UserRepresentation.class);
        RoleMappingResource mockRoleMappingResource = mock(RoleMappingResource.class);
        MappingsRepresentation mockMappingsRepresentation = mock(MappingsRepresentation.class);
        List<GroupRepresentation> mockGroupRepresentation = mock(List.class);
        List<RoleRepresentation> mockedRoleRepresentation = mock(List.class);

        when(mockedUsersResource.get(String.valueOf(userUUID))).thenReturn(mockedUserResource);
        when(mockedUserResource.toRepresentation()).thenReturn(mockedUserRepresentation);
        when(mockedUserResource.roles()).thenReturn(mockRoleMappingResource);
        when(mockRoleMappingResource.getAll()).thenReturn(mockMappingsRepresentation);
        when(mockMappingsRepresentation.getRealmMappings()).thenReturn(mockedRoleRepresentation);
        when(mockedUserResource.groups()).thenReturn(mockGroupRepresentation);

        userService.getUserById(userUUID);
        UserResponse expectedUserResponse = userMapper.userRepresentationToUserResponse(mockedUserRepresentation, mockedRoleRepresentation, mockGroupRepresentation);
        UserResponse actualUserResponse = userService.getUserById(userUUID);

        Assertions.assertEquals(expectedUserResponse, actualUserResponse);
    }

    @Test
    public void getUserByIdShouldReturn404WhenUserDoesNotExist() {
        UUID invalidUUID = UUID.randomUUID();

        when(mockedUsersResource.get(String.valueOf(invalidUUID))).thenThrow(new NotFoundException("User not found"));

        BackendResourcesException exception = assertThrows(BackendResourcesException.class,
                () -> userService.getUserById(invalidUUID));

        Assertions.assertEquals("User not found", exception.getMessage());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
    }
}