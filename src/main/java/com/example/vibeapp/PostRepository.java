package com.example.vibeapp;

import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepository {
    private final List<Post> posts = new ArrayList<>();
    private long sequence = 0L;

    public PostRepository() {
        initData();
    }

    private void initData() {
        for (long i = 1; i <= 10; i++) {
            save(new Post(
                null,
                "바이브코딩에 오신 것을 환영합니다 " + i,
                "이것은 예제 게시글 내용입니다 " + i,
                LocalDateTime.now().minusDays(10 - i),
                LocalDateTime.now().minusDays(10 - i),
                (int) (Math.random() * 100)
            ));
        }
    }

    public void save(Post post) {
        post.setNo(++sequence);
        posts.add(post);
    }

    public List<Post> findAll() {
        return new ArrayList<>(posts);
    }

    public Post findByNo(Long no) {
        return posts.stream()
                .filter(post -> post.getNo().equals(no))
                .findFirst()
                .orElse(null);
    }
}
