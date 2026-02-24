package com.example.vibeapp.post.dto;

import com.example.vibeapp.post.Post;
import java.time.LocalDateTime;

public record PostResponseDto(
    Long no,
    String title,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Integer views
) {
    public static PostResponseDto from(Post post) {
        return new PostResponseDto(
                post.getNo(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getViews()
        );
    }
}
