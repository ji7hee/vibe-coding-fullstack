package com.example.vibeapp;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getPostList(int page, int size) {
        List<Post> allPosts = postRepository.findAll().stream()
                .sorted(Comparator.comparing(Post::getNo).reversed())
                .collect(Collectors.toList());
        
        int fromIndex = (page - 1) * size;
        if (fromIndex >= allPosts.size()) {
            return List.of();
        }
        
        int toIndex = Math.min(fromIndex + size, allPosts.size());
        return allPosts.subList(fromIndex, toIndex);
    }

    public int getTotalPages(int size) {
        int totalPosts = postRepository.findAll().size();
        return (int) Math.ceil((double) totalPosts / size);
    }

    public Post getPost(Long no) {
        Post post = postRepository.findByNo(no);
        if (post != null) {
            post.setViews(post.getViews() + 1);
        }
        return post;
    }

    public Post getPostWithoutViewCount(Long no) {
        return postRepository.findByNo(no);
    }

    public void addPost(String title, String content) {
        Post post = new Post(
                null,
                title,
                content,
                LocalDateTime.now(),
                null,
                0
        );
        postRepository.save(post);
    }

    public void updatePost(Long no, String title, String content) {
        Post post = postRepository.findByNo(no);
        if (post != null) {
            post.setTitle(title);
            post.setContent(content);
            post.setUpdatedAt(LocalDateTime.now());
        }
    }

    public void deletePost(Long no) {
        postRepository.delete(no);
    }
}
