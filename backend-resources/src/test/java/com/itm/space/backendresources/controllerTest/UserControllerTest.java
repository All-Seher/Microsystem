package com.itm.space.backendresources.controllerTest;

import com.itm.space.backendresources.BaseIntegrationTest;
import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.api.response.UserResponse;
import com.itm.space.backendresources.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest extends BaseIntegrationTest {

    final String USERNAME = "test-username";
    final String FIRST_NAME = "test-user";
    final String LAST_NAME = "test-lastName";
    final String EMAIL = "test@gmail.com";
    final String PASSWORD = "test-password";
    final List<String> ROLES = List.of("MODERATOR");
    final List<String> GROUPS = List.of("Moderators");

    @MockBean
    private UserService userService;

    private UserRequest userRequest;
    private UserResponse userExpectResponse;
    private UUID userUUID;

    @BeforeEach
    void setUp() {
        userUUID = UUID.randomUUID();

        userRequest = UserRequest.builder().
                username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME).build();

        userExpectResponse = UserResponse.builder().
                firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .groups(GROUPS)
                .roles(ROLES).build();
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void shouldCreateNewUserWithValidData() throws Exception {
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnErrorWithoutRoles() throws Exception {
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "test-user", roles = "MODERATOR")
    public void shouldGetUserNameTest() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/api/users/hello")).andExpect(status().isOk()).andReturn();

        String body = mvcResult.getResponse().getContentAsString();

        assert body.equals("test-user");
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void shouldReturnUserObjectWhenValidUUID() throws Exception {
        given(userService.getUserById(userUUID)).willReturn(userExpectResponse);

        MvcResult mvcResult = mvc.perform(get("/api/users/{userUUID}", userUUID))
                .andExpect(status().isOk()).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();

        UserResponse actualUserResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), UserResponse.class);

        assertEquals(actualUserResponse.getFirstName(), userExpectResponse.getFirstName());
        assertEquals(actualUserResponse.getLastName(), userExpectResponse.getLastName());
        assertEquals(actualUserResponse.getEmail(), userExpectResponse.getEmail());
        assertEquals(actualUserResponse.getRoles(), userExpectResponse.getRoles());
        assertEquals(actualUserResponse.getGroups(), userExpectResponse.getGroups());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void shouldReturnNotFoundWhenInvalidUUID() throws Exception {
        given(userService.getUserById(userUUID)).willReturn(userExpectResponse);

        mvc.perform(get("/api/users/{userUUID}", "1"))
                .andExpect(status().is4xxClientError());
    }
}
