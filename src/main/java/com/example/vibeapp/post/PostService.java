package com.example.vibeapp.post;

import com.example.vibeapp.post.dto.PostCreateDto;
import com.example.vibeapp.post.dto.PostListDto;
import com.example.vibeapp.post.dto.PostResponseDto;
import com.example.vibeapp.post.dto.PostUpdateDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostMapper postMapper;
    private final PostTagRepository postTagRepository;

    public PostService(PostMapper postMapper, PostTagRepository postTagRepository) {
        this.postMapper = postMapper;
        this.postTagRepository = postTagRepository;
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
            return PostResponseDto.from(post, getTagsAsString(no));
        }
        return null;
    }

    public PostResponseDto findByIdWithoutViewCount(Long no) {
        Post post = postMapper.findById(no);
        return post != null ? PostResponseDto.from(post, getTagsAsString(no)) : null;
    }

    private String getTagsAsString(Long postNo) {
        List<PostTag> tags = postTagRepository.findByPostNo(postNo);
        return tags.stream()
                .map(PostTag::getTagName)
                .collect(Collectors.joining(", "));
    }

    @Transactional
    public void create(PostCreateDto createDto) {
        Post post = createDto.toEntity();
        postMapper.insert(post);
        saveTags(post.getNo(), createDto.tags());
    }

    @Transactional
    public void update(Long no, PostUpdateDto updateDto) {
        Post post = postMapper.findById(no);
        if (post != null) {
            updateDto.updateEntity(post);
            postMapper.update(post);
            
            postTagRepository.deleteByPostNo(no);
            saveTags(no, updateDto.tags());
        }
    }

    private void saveTags(Long postNo, String tagsString) {
        if (tagsString == null || tagsString.isBlank()) {
            return;
        }
        
        String[] tags = tagsString.split(",");
        for (String tagName : tags) {
            String trimmedTag = tagName.trim();
            if (!trimmedTag.isEmpty()) {
                postTagRepository.save(new PostTag(null, postNo, trimmedTag));
            }
        }
    }

    public void delete(Long no) {
        postMapper.delete(no);
    }
}
