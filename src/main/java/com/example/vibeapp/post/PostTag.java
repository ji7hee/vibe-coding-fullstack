package com.example.vibeapp.post;

import jakarta.persistence.*;

/**
 * 게시글 태그(POST_TAGS) 테이블과 매핑되는 JPA 엔티티 클래스
 */
@Entity
@Table(name = "POST_TAGS")
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "post_no", nullable = false)
    private Long postNo;

    @Column(name = "tag_name", nullable = false, length = 50)
    private String tagName;

    // JPA는 기본 생성자가 필요함
    protected PostTag() {}

    public PostTag(Long id, Long postNo, String tagName) {
        this.id = id;
        this.postNo = postNo;
        this.tagName = tagName;
    }

    // Getters
    public Long getId() { return id; }
    public Long getPostNo() { return postNo; }
    public String getTagName() { return tagName; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setPostNo(Long postNo) { this.postNo = postNo; }
    public void setTagName(String tagName) { this.tagName = tagName; }
}
