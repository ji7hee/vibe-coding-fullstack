package com.example.vibeapp.post.dto;

import com.example.vibeapp.post.Post;
import java.time.LocalDateTime;

public class PostListDto {
    private Long no;
    private String title;
    private LocalDateTime createdAt;
    private Integer views;

    private PostListDto(Long no, String title, LocalDateTime createdAt, Integer views) {
        this.no = no;
        this.title = title;
        this.createdAt = createdAt;
        this.views = views;
    }

    public static PostListDto from(Post post) {
        return new PostListDto(
                post.getNo(),
                post.getTitle(),
                post.getCreatedAt(),
                post.getViews()
        );
    }

    public Long getNo() { return no; }
    public String getTitle() { return title; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Integer getViews() { return views; }
}
