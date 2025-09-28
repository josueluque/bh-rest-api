package com.backend.rest_api.domain.dto;

import lombok.Data;
import java.util.List;

@Data
public class DetailResponseDTO {
    private PostResponseDTO post;
    private UserResponseDTO user;
    private List<CommentResponseDTO> comments;


    public PostResponseDTO getPost() {
        return post;
    }
    public void setPost(PostResponseDTO post) {
        this.post = post;
    }
    public UserResponseDTO getUser() {
        return user;
    }
    public void setUser(UserResponseDTO user) {
        this.user = user;
    }
    public List<CommentResponseDTO> getComments() {
        return comments;
    }
    public void setComments(List<CommentResponseDTO> comments) {
        this.comments = comments;
    }
}
