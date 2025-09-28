package com.backend.rest_api.services;

import com.backend.rest_api.domain.Post;
import com.backend.rest_api.domain.User;
import com.backend.rest_api.domain.dto.UserResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String usersByUserId;
    private static final Logger log = LoggerFactory.getLogger(PostService.class);

    public UserService(
            RestTemplate restTemplate,
            @Value("${jsonplaceholder.base-url:https://jsonplaceholder.typicode.com}")
            String baseUrl,
            @Value("${jsonplaceholder.usersByUserId:/users/{userId}}")
            String usersByUserId
    ) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.usersByUserId = usersByUserId;
    }

    public User getUserById(Integer userId) {
        String url = baseUrl + usersByUserId.replace("{userId}", userId.toString());
        log.info("Obteniendo usuario con ID {} desde URL: {}", userId, url);
        return restTemplate.getForObject(url, User.class);
    }

    public Map<Integer, User> getUsersByPosts(List<Post> posts) {
        log.info("Obteniendo usuarios para {} posts", posts.size());
        Map<Integer, User> users = new HashMap<>();
        for (Post post : posts) {
            users.computeIfAbsent(post.getUserId(), this::getUserById);
        }
        log.info("Usuarios obtenidos: {}", users.size());
        return users;
    }

    public UserResponseDTO toUserResponseDTO(User user){
        log.debug("Convirtiendo usuario {} a DTO", user.getId());
        UserResponseDTO userDto = new UserResponseDTO();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

}
