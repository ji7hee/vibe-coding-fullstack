package com.example.vibeapp.post.dto;

import com.example.vibeapp.post.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record PostCreateDto(
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 100, message = "제목은 100자 이내여야 합니다.")
    String title,
    
    String content,
    
    String tags
) {
    public PostCreateDto() {
        this(null, null, null);
    }
    
    public Post toEntity() {
        return new Post(
                null,
                this.title,
                this.content,
                LocalDateTime.now(),
                null,
                0
        );
    }
}
