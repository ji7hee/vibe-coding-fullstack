package com.example.vibeapp.post;

import com.example.vibeapp.post.dto.PostCreateDto;
import com.example.vibeapp.post.dto.PostListDto;
import com.example.vibeapp.post.dto.PostResponseDto;
import com.example.vibeapp.post.dto.PostUpdateDto;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostMapper postMapper;

    public PostService(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    public List<PostListDto> findAll(int page, int size) {
        List<Post> allPosts = postMapper.findAll();
        
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
        int totalPosts = postMapper.count();
        return (int) Math.ceil((double) totalPosts / size);
    }

    public PostResponseDto findById(Long no) {
        Post post = postMapper.findById(no);
        if (post != null) {
            postMapper.updateViews(no);
            post.setViews(post.getViews() + 1);
            return PostResponseDto.from(post);
        }
        return null;
    }

    public PostResponseDto findByIdWithoutViewCount(Long no) {
        Post post = postMapper.findById(no);
        return post != null ? PostResponseDto.from(post) : null;
    }

    public void create(PostCreateDto createDto) {
        postMapper.insert(createDto.toEntity());
    }

    public void update(Long no, PostUpdateDto updateDto) {
        Post post = postMapper.findById(no);
        if (post != null) {
            updateDto.updateEntity(post);
            postMapper.update(post);
        }
    }

    public void delete(Long no) {
        postMapper.delete(no);
    }
}
