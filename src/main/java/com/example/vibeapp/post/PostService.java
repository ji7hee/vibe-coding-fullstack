package com.example.vibeapp.post;

import com.example.vibeapp.post.dto.PostCreateDto;
import com.example.vibeapp.post.dto.PostListDto;
import com.example.vibeapp.post.dto.PostResponseDto;
import com.example.vibeapp.post.dto.PostUpdateDto;
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

    public List<PostListDto> findAll(int page, int size) {
        List<Post> allPosts = postRepository.findAll().stream()
                .sorted(Comparator.comparing(Post::getNo).reversed())
                .collect(Collectors.toList());
        
        int fromIndex = (page - 1) * size;
        if (fromIndex >= allPosts.size()) {
            return List.of();
        }
        
        int toIndex = Math.min(fromIndex + size, allPosts.size());
        return allPosts.subList(fromIndex, toIndex).stream()
                .map(PostListDto::from)
                .collect(Collectors.toList());
    }

    public int getTotalPages(int size) {
        int totalPosts = postRepository.findAll().size();
        return (int) Math.ceil((double) totalPosts / size);
    }

    public PostResponseDto findById(Long no) {
        Post post = postRepository.findById(no);
        if (post != null) {
            post.setViews(post.getViews() + 1);
            return PostResponseDto.from(post);
        }
        return null;
    }

    public PostResponseDto findByIdWithoutViewCount(Long no) {
        Post post = postRepository.findById(no);
        return post != null ? PostResponseDto.from(post) : null;
    }

    public void create(PostCreateDto createDto) {
        postRepository.save(createDto.toEntity());
    }

    public void update(Long no, PostUpdateDto updateDto) {
        Post post = postRepository.findById(no);
        if (post != null) {
            updateDto.updateEntity(post);
        }
    }

    public void delete(Long no) {
        postRepository.delete(no);
    }
}
