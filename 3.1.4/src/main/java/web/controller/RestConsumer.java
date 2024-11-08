package web.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import web.model.User;

@RequiredArgsConstructor
@Component
public class RestConsumer {
    private final RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        StringBuilder code = new StringBuilder();
        String url = "http://94.198.50.185:7081/api/users";

        String cookie = getUsers(url);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);

        code.append(postUsers(url, headers));
        code.append(putUsers(url, headers));
        code.append(deleteUser(url, headers));
        System.out.println("КОД: " + code);
    }

    private String getUsers(String url) {
        return restTemplate.getForEntity(url, String.class).getHeaders().getFirst("Set-Cookie");
    }

    private String postUsers(String url, HttpHeaders headers) {
        HttpEntity<User> user = new HttpEntity<>(
                User.builder()
                        .id(3)
                        .name("James")
                        .lastName("Brown")
                        .age(1).build(),
                headers);

        return restTemplate.postForEntity(url, user, String.class).getBody();
    }

    private String putUsers(String url, HttpHeaders headers) {
        HttpEntity<User> user = new HttpEntity<>(
                User.builder()
                        .id(3)
                        .name("Thomas")
                        .lastName("Brown")
                        .age(1).build(),
                headers);

        return restTemplate.exchange(url, HttpMethod.PUT, user, String.class).getBody();
    }

    private String deleteUser(String url, HttpHeaders headers) {
        return restTemplate.exchange(url + "/3", HttpMethod.DELETE, new HttpEntity<>(headers), String.class).getBody();
    }
}
