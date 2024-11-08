package web.dto;

import lombok.Getter;
import lombok.Setter;
import web.annotation.Unique;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDto {

    @Unique
    private String username;
    private String surname;

    private List<String> roles = new ArrayList<>();
}
