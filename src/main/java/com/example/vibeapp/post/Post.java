package com.example.vibeapp.post;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 게시글(POSTS) 테이블과 매핑되는 JPA 엔티티 클래스
 */
@Entity
@Table(name = "POSTS")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long no;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "CLOB")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "views")
    private Integer views;

    // JPA는 기본 생성자가 필요함 (protected 또는 public)
    protected Post() {}

    public Post(Long no, String title, String content,
                LocalDateTime createdAt, LocalDateTime updatedAt, Integer views) {
        this.no = no;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.views = views;
    }

    // Getters
    public Long getNo() { return no; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public Integer getViews() { return views; }

    // Setters
    public void setNo(Long no) { this.no = no; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setViews(Integer views) { this.views = views; }
}
