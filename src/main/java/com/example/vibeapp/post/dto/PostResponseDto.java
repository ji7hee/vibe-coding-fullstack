package com.example.vibeapp.post.dto;

import com.example.vibeapp.post.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record PostResponseDto(
    Long no,
    String title,
    String content,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime updatedAt,
    Integer views,
    String tags
) {
    public static PostResponseDto from(Post post) {
        return from(post, "");
    }

    public static PostResponseDto from(Post post, String tags) {
        return new PostResponseDto(
                post.getNo(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getViews(),
                tags
        );
    }
}
