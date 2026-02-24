package com.example.vibeapp.post;

public class PostTag {
    private Long id;
    private Long postNo;
    private String tagName;

    public PostTag() {
    }

    public PostTag(Long id, Long postNo, String tagName) {
        this.id = id;
        this.postNo = postNo;
        this.tagName = tagName;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getPostNo() {
        return postNo;
    }

    public String getTagName() {
        return tagName;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setPostNo(Long postNo) {
        this.postNo = postNo;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
