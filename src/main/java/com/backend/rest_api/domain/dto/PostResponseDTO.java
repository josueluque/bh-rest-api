package com.backend.rest_api.domain.dto;

import lombok.Data;

@Data
public class PostResponseDTO {
    private Integer id;
    private String title;
    private String body;

    public Integer getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
}
