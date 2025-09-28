package com.backend.rest_api.controllers;

import com.backend.rest_api.domain.Comment;
import com.backend.rest_api.domain.Post;
import com.backend.rest_api.domain.User;
import com.backend.rest_api.domain.dto.DetailResponseDTO;
import com.backend.rest_api.services.CommentService;
import com.backend.rest_api.services.PostService;
import com.backend.rest_api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostService.class);

    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;

    @GetMapping("/posts")
    @Operation(summary = "Obtiene detalle de publicaciones mediante paginacion utilizando datos de publicaciones, comentarios y usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle de posts obtenidos correctamente"),
            @ApiResponse(responseCode = "204", description = "Sin contenido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")

    })
    public ResponseEntity<List<DetailResponseDTO>> getPostsDetail(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<Post> posts = postService.getPosts(page, size);

        if (posts.isEmpty()) {
            log.info("No se obtuvieron post para la pagina {}", page);
            return ResponseEntity.noContent().build();
        }

        Map<Integer, List<Comment>> postsComments = commentService.getCommentsByPosts(posts);

        Map<Integer, User> usersMap = userService.getUsersByPosts(posts);

        return postService.getPostsDetail(posts, postsComments, usersMap);
    }

    @DeleteMapping("/posts/{id}")
    @Operation(summary = "Elimina un post por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Eliminado correctamente, sin contenido"),
            @ApiResponse(responseCode = "404", description = "No encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")

    })
    public ResponseEntity<Void> deletePostById(
            @PathVariable int id
    ) {
        return postService.deletePostById(id);
    }
}