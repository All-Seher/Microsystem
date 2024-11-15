package com.itm.space.backendresources.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private List<String> roles;
    private List<String> groups;
}
