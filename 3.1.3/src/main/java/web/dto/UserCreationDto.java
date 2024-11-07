package web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import web.annotation.Unique;

import java.util.List;

@Getter
@Setter
@ToString
public class UserCreationDto {

    @Unique
    private String username;
    private String surname;
    private String password;
    private List<String> roles;
}