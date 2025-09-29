package com.backend.rest_api.services;

import com.backend.rest_api.domain.Comment;
import com.backend.rest_api.domain.Post;
import com.backend.rest_api.domain.User;
import com.backend.rest_api.domain.dto.DetailResponseDTO;
import com.backend.rest_api.domain.dto.PostResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class PostService {
    private final RestTemplate restTemplate;
    private final UserService userService;
    private final CommentService commentService;
    private final String baseUrl;
    private final String allposts;
    private final String postByPostId;
    private static final Logger log = LoggerFactory.getLogger(PostService.class);

    public PostService(
            RestTemplate restTemplate,
            UserService userService,
            CommentService commentService,
            @Value("${jsonplaceholder.base-url:https://jsonplaceholder.typicode.com}")
            String baseUrl,
            @Value("${jsonplaceholder.posts:/posts}")
            String allposts,
            @Value("${jsonplaceholder.postByPost:/posts/{postId}}")
            String postByPostId
    ) {
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.commentService = commentService;
        this.baseUrl = baseUrl;
        this.allposts = allposts;
        this.postByPostId = postByPostId;
    }

    public List<Post> getPosts(
            int page,
            int size
    ) {
        String url = baseUrl + allposts;
        log.info("Obteniendo posts desde URL: {}", url);
        Post[] allPostsArray = restTemplate.getForObject(url, Post[].class);

        if (allPostsArray == null || allPostsArray.length == 0){
            log.info("No se encontraron posts");
            return List.of();
        }

        List<Post> postsList = Arrays.asList(allPostsArray);

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, postsList.size());

        if (fromIndex >= postsList.size()) {
            log.info("Pagina {} fuera de rango para {} posts", page, postsList.size());
            return List.of();
        }
        log.info("Posts obtenidos exitosamente");
        return postsList.subList(fromIndex, toIndex);
    }


    public PostResponseDTO toPostResponseDTO(Post post){
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setBody(post.getBody());
        return dto;
    }
    public DetailResponseDTO toDetailResponseDTO (
            Post post,
            Map<Integer, List<Comment>> comments,
            Map<Integer, User> usersMap)
    {
        log.info("Convirtiendo Post {}, user {} y comentarios a DetailResponseDTO", post.getId(), post.getUserId());
        User user = usersMap.get(post.getUserId());
        List<Comment> commentsList = comments.get(post.getId());

        DetailResponseDTO response = new DetailResponseDTO();
        response.setPost(toPostResponseDTO(post));
        response.setUser(userService.toUserResponseDTO(user));
        response.setComments(
                commentsList.stream()
                        .map(commentService::toCommentResponseDTO)
                        .toList()
        );
        return response;
    }

    public ResponseEntity<List<DetailResponseDTO>> getPostsDetail(
            List<Post> posts,
            Map<Integer, List<Comment>> comments,
            Map<Integer, User> usersMap
    ){
        log.info("Generando detalle de posts");
        try {
            List<DetailResponseDTO> result = posts.stream().map(post -> toDetailResponseDTO(post, comments, usersMap)).toList();

            if (posts.isEmpty()) {return ResponseEntity.noContent().build();}

            log.info("Detalle de posts generado exitosamente");
            return ResponseEntity.ok(result);
        } catch (RestClientException e) {
            log.error("Error al generar detalle de posts: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    public ResponseEntity<Void> deletePostById(Integer postId){
        String url = baseUrl + postByPostId.replace("{postId}", postId.toString());
        log.info("Eliminando post con ID {} desde URL: {}", postId, url);
        try {
            restTemplate.getForObject(url, Post.class);

            restTemplate.delete(url);

            log.info("Post con ID {} eliminado correctamente", postId);
            return ResponseEntity.noContent().build();

        } catch (HttpClientErrorException.NotFound e) {
            log.info("Post con ID {} no encontrado para eliminar", postId);
            return ResponseEntity.notFound().build();
        }catch (RestClientException e) {
            log.error("Error eliminando post con ID {}: {}", postId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
