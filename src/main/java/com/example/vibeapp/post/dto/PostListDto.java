package com.example.vibeapp.post.dto;

import com.example.vibeapp.post.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record PostListDto(
    Long no,
    String title,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
    Integer views
) {
    public static PostListDto from(Post post) {
        return new PostListDto(
                post.getNo(),
                post.getTitle(),
                post.getCreatedAt(),
                post.getViews()
        );
    }
}
